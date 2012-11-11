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
    Item = collections.namedtuple("Item", "prefix setters")

    prefixes = collections.defaultdict(set)
    for f in formulas:
        for p in iter_prefixes(f, start=2):
            prefixes[p].add(".startsCompound(true)")
        prefixes[f.text].add(".isFinal(true)")

    prefixes = [Item(p, sorted(s)) for p, s in prefixes.iteritems()]
    prefixes.sort(key=lambda x: x.prefix)

    return prefixes


def gen_productions(formulas):
    Prod = collections.namedtuple("Prod", "prefix term result")
    produced = set()
    for terms in (f.terms for f in formulas):
        for i in range(1, len(terms)):
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
        % for i, s in enumerate(it.setters):
            ${s}${";" if i == len(it.setters) - 1 else ""}
        % endfor
    % endfor

    % for it in gen_productions(group):
        P("${it.prefix}", "${it.term}", "${it.result}");
    % endfor
    }
% endfor

    static {
% for term, count in stats:
    % if "A" <= term[0] <= "Z" or term in "([":
        E("${term}")
            .startsCompound(true);
    % else:
        E("${term}");
    % endif
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
