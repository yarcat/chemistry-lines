<%doc>
Arguments:
    name -- class name,
    formulas -- list of formula (text, terms) pairs.
</%doc>\
<%
import collections
import itertools

def split_formula_set(formulas):
    """Split all formulas into groups to pass java method code limit 64K

    Group formulas by first letter.

    """
    def group_key(formula):
        s = formula.text
        return s[1] if s[0] in "([" else s[0]

    formulas = sorted(formulas, key=lambda f: (group_key(f), f.text))
    for key, grp in itertools.groupby(formulas, key=group_key):
        yield key, tuple(grp)


def iter_prefixes(formula, start=1, end=0):
    """Yield all formula prefixes as strings"""
    for i in range(start, len(formula.terms) + end):
        yield "".join(formula.terms[:i])


def gen_prefixes(formulas):
    Item = collections.namedtuple("Item", "prefix is_final")

    # let's keep the sequence of formulas
    seen = set()
    for f in formulas:
        for prefix in iter_prefixes(f):
            if prefix not in seen:
                yield Item(prefix, False)
                seen.add(prefix)
        yield Item(f.text, True)


def gen_productions(formulas):
    Prod = collections.namedtuple("Prod", "prefix term result")
    produced = set()
    for terms in (f.terms for f in formulas):
        for i in range(1, len(terms) - 1):
            p = Prod("".join(terms[:i]), terms[i], "".join(terms[:i+1]))
            if p not in produced:
                yield p
                produced.add(p)

%>\
package com.yarcat.chemistrylines.field;

/** Static definition of all known elements and their productions. */
public final class ${name} {

    public final static ElementRegistry contents = new ElementRegistry();
    public final static WeightedArrayOfStrings terms =
            new WeightedArrayOfStrings(${len(stats)});
\
<% keys = [] %> \
% for key, group in split_formula_set(formulas):
    <% keys.append(key) %> \

    private final static void register${key}() {
    % for it in gen_prefixes(group):
        E("${it.prefix}")
        % if it.is_final:
            .isFinal(true);
        % else:
            .startsCompound(true);
        % endif
    % endfor

    % for it in gen_productions(group):
        P("${it.prefix}", "${it.term}", "${it.result}");
    % endfor
    }
% endfor

    static {
% for term, count in stats:
        terms.add("${term}", ${count});
% endfor

% for key in keys:
        register${key}();
% endfor
    }

    /** Registers element with the given id and name. */
    private final static Element E(String id, String name) {
        Element e = new Element(id, name);
        contents.register(e);
        return e;
    }

    /** Registers element, sets name to its id. */
    private final static Element E(String id) {
        return E(id, id);
    }

    /** Registers production. */
    private final static void P(String id1, String id2, String... ids) {
        contents.register(id1, id2, ids);
    }
}\
## vim: set ft=mako :
