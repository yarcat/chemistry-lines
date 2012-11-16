<%doc>
Arguments:
    name -- class name,
    formulas -- list of formula (text, terms) pairs.
</%doc>\
<% import registry_helper as R %>\
package com.yarcat.chemistrylines.field;

/** Static definition of all known elements and their productions. */
public final class ${name} {

    public final static ElementRegistry contents = new ElementRegistry();
    public final static WeightedArrayOfStrings terms =
            new WeightedArrayOfStrings(${len(stats)});
\
<% keys = [] %> \
% for key, group in R.split_formula_set(formulas):
    <% keys.append(key) %> \

    private final static void register${key}() {
    % for it in R.gen_prefixes(group):
        E("${it.prefix}")
        % for i, s in enumerate(it.setters):
            ${s}${";" if i == len(it.setters) - 1 else ""}
        % endfor
    % endfor

    % for it in R.gen_productions(group):
        P("${it.prefix}", "${it.term}", "${it.result}");
    % endfor
    }
% endfor

    static {
% for term, count in stats:
    % if term.starts_formula:
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
        if (contents.contains(id)) {
            return contents.get(id);
        }
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
