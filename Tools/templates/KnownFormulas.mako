<%doc>
Arguments:
    name -- class name,
    formulas -- list of formula (text, terms) pairs.
</%doc>\
<%include file="stamp.mako"/>\
<% import registry_helper as R %>\
package com.yarcat.chemistrylines.field;

import java.util.HashMap;
import java.util.Map;

import com.yarcat.chemistrylines.field.Element.Category;
import com.yarcat.chemistrylines.field.Element.StateOfMatter;

/** Static definition of all known formulas. */
public final class ${name} {

    public final static ElementRegistry contents;
    public final static Map<Element, Element[]> formulaTerms;
    public final static WeightedArrayOfStrings terms;
\
<% keys = [] %> \
% for key, group in R.split_formula_set(formulas):
    <% keys.append(key) %> \

    private final static void register${key}() {
    % for it in R.gen_prefixes(group):
        % if it.setters:
            % if it.formula:
        E("${it.prefix}", "${it.formula.text}")
            % else:
        E("${it.prefix}")
            % endif
            % for i, s in enumerate(it.setters):
            ${s}${";" if i == len(it.setters) - 1 else ""}
            % endfor
        % else:
        ## only formulas could get here
        E("${it.prefix}", "${it.formula.text if it.formula else ""}"); // does not match final conditions
        % endif
    % endfor

    % for it in R.gen_productions(group):
        P("${it.prefix}", "${it.term}", "${it.result}");
    % endfor

    % for f in group:
        % if f.is_final():
<%            t = '"%s"' % '", "'.join(map(str, f.terms)) %>\
        F("${f.prefix()}", ${t});
        % endif
    % endfor
    }
% endfor

    static {
        contents = new ElementRegistry();
        formulaTerms = new HashMap<Element, Element[]>();
        terms = new WeightedArrayOfStrings(${len(final_formula_stats)});

% for term in R.collect_terms(formulas):
        E("${term}")\
    % if term.starts_formula:

            .startsCompound(true)\
    % endif
    % if term.element:

            .category(Category.${term.element.category})
            .stateOfMatter(StateOfMatter.${term.element.state_of_matter})\
    % endif
;
% endfor

        // Terminals that could form a final formula.
% for term, count in final_formula_stats:
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
    private final static void F(String id, String... terms) {
        Element[] t = new Element[terms.length];
        for(int i = 0; i < terms.length; ++i) {
            t[i] = E(terms[i]);
        }
        formulaTerms.put(contents.get(id), t);
    }

    /** Registers production. */
    private final static void P(String id1, String id2, String... ids) {
        contents.register(id1, id2, ids);
    }
}
## vim: set ft=mako :
