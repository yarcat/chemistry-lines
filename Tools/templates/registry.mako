<%doc>
Arguments:
    name -- class name,
    formulas -- list of formula (text, terms) pairs.
</%doc>\
<%
import collections
import itertools
import operator as op

def split_formula_set(formulas):
    # """Split all formulas into groups to pass java method code limit 64K
    #
    # Group formulas by first letter.
    #
    # """
    def group_key(formula):
        s = formula.text
        return s[1] if s[0] in "([" else s[0]

    formulas = sorted(formulas, key=lambda fml: (group_key(fml), fml.text))
    for key, grp in itertools.groupby(formulas, key=group_key):
        yield key, tuple(grp)


def iter_prefixes(formula, start=1, end=0):
    """Yield all formula prefixes as strings"""
    for ii in range(start, len(formula.terms) + end):
        yield "".join(formula.terms[:ii])


def gen_prefixes(formulas):
    Item = collections.namedtuple("Item", "prefix is_final")

    # let's keep the sequence of formulas
    seen = set()
    for formula in formulas:
        for prefix in iter_prefixes(formula):
            if prefix not in seen:
                yield Item(prefix, False)
                seen.add(prefix)
        yield Item(formula.text, True)


def gen_productions(formulas):
    Prod = collections.namedtuple("Prod", "prefix term result")
    for terms in map(op.attrgetter("terms"), formulas):
        for ii in range(1, len(terms) - 1):
            yield Prod("".join(terms[:ii]), terms[ii], "".join(terms[:ii+1]))

%>\
package com.yarcat.chemistrylines.field;

/** Static definition of all known elements and their productions. */
public final class ${name} {

    public final static ElementRegistry contents = new ElementRegistry();
    public final static String[] terms = {
% for term, count in stats[:-1]:
        "${term}",
% endfor
        "${stats[-1].term}"
    };
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
