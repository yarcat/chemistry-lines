<%doc>
Arguments:
    name -- class name,
    formulas -- list of formula (text, terms) pairs.
</%doc>\
<% import registry_helper as R %>\
package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.ChemicalReactor;
import com.yarcat.chemistrylines.algorithms.CompoundRemover;
import com.yarcat.chemistrylines.algorithms.SimpleReactor;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.KnownFormulas;
import com.yarcat.chemistrylines.field.RectField;

public class ${name} {

    ElementRegistry mRegistry;
    CompoundRemover mRemover;
    ChemicalReactor mReactor;

    @Before
    public void setUp() {
        mRegistry = KnownFormulas.contents;
        mReactor = new SimpleReactor(mRegistry);
        mRemover = new CompoundRemover(mReactor);
    }
\
% for key, group in R.split_formula_set(formulas):

    @Test
    public void test${key}() {
    % for f in group:
<%        t = '"%s"' % '", "'.join(map(str, f.terms)) %>\
        % if len(f) == 1:
            ## Terminals that are also formulas should stay on the board.
            ## For plain-formulas it stands for oxygen only. See #23.
            ## For compact-formulas(#10) there are many of them.
        assertFalse(formulaIsRemoved(${t}));
        % else:
            % if f.prefix() != f.text:
        // ${f.text}
            % endif
        assertTrue(reactionProduces("${f.prefix()}", ${t}));
        assertTrue(formulaIsRemoved(${t}));
        % endif
    % endfor
    }
% endfor

    private boolean formulaIsRemoved(String... fieldMap) {
        Field field = new RectField(fieldMap.length, 1);
        for (int i = 0; i < fieldMap.length; ++i) {
            field.at(i).setElement(mRegistry.get(fieldMap[i]));
        }
        mRemover.removeAllCompounds(field);

        for (int i = 0; i < fieldMap.length; ++i) {
            if (!field.at(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean reactionProduces(String what, String... terms) {
        ArrayList<Element> elements = new ArrayList<Element>(terms.length);
        for (String it : terms) {
            elements.add(mRegistry.get(it));
        }

        ArrayList<Element> productions = mReactor.getCompounds(elements);
        Element target = mRegistry.get(what);
        return productions.contains(target);
    }
}
## vim: set ft=mako :
