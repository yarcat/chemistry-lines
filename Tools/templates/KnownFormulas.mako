<%doc>
Arguments:
    name -- class name,
    formulas -- list of formula (text, terms) pairs.
</%doc>\
<% import registry_helper as R %>\
package com.yarcat.chemistrylines.field;

/** Static definition of all known formulas. */
public final class ${name} {

    public final static ElementRegistry contents = new ElementRegistry();
    public final static Element[][] formulaTerms = new Element[${len(formulas)}][];
    public final static WeightedArrayOfStrings terms =
            new WeightedArrayOfStrings(${len(stats)});
\
<% keys = [] %> \
<% n = 0 %> \
% for key, group in R.split_formula_set(formulas):
    <% keys.append(key) %> \

    private final static void register${key}() {
    % for it in R.gen_prefixes(group):
        % if it.setters:
        E("${it.prefix}")
            % for i, s in enumerate(it.setters):
            ${s}${";" if i == len(it.setters) - 1 else ""}
            % endfor
        % else:
        E("${it.prefix}"); // does not match final conditions
        % endif
    % endfor

    % for it in R.gen_productions(group):
        P("${it.prefix}", "${it.term}", "${it.result}");
    % endfor

    % for f in group:
<%        t = '"%s"' % '", "'.join(map(str, f.terms)) %>\
        F(${n}, ${t});
<%        n += 1 %>\
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

    /** Adds formula and its terminals */
    private final static void F(int n, String... terms) {
        Element[] t = new Element[terms.length];
        for(int i = 0; i < terms.length; ++i) {
            t[i] = E(terms[i]);
        }
        formulaTerms[n] = t;
    }

    /** Registers production. */
    private final static void P(String id1, String id2, String... ids) {
        contents.register(id1, id2, ids);
    }
}\
## vim: set ft=mako :
