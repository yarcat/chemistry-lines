<%doc>
Arguments:
    name -- class name,
    formulas -- list of formula (text, terms) pairs.
</%doc>\
<%include file="stamp.mako"/>\
<% import registry_helper as R %>\
package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.ChemicalReactor;
import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.algorithms.CompoundScanner;
import com.yarcat.chemistrylines.algorithms.SimpleReactor;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementRegistry;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.KnownFormulas;
import com.yarcat.chemistrylines.field.RectField;

public class KnownFormulasTest {

    ElementRegistry mRegistry;
    ChemicalReactor mReactor;
    CompoundScanner mScanner;
    Map<Element, Element[]> mFormulaTerms;

    @Before
    public void setUp() {
        mFormulaTerms = KnownFormulas.formulaTerms;
        mRegistry = KnownFormulas.contents;
        mReactor = new SimpleReactor(mRegistry);
        mScanner = new CompoundScanner(mReactor);
    }

% for key, group in R.split_formula_set(formulas):
<%    group = list(group) %>

    @Test
    public void test${key}() {
        Element e;
\
<%    one_terminal_formulas = [f for f in group if len(f) == 1]%>\
    % if one_terminal_formulas:
        // Terminals that are also formulas should stay on the board.
        ## For plain-formulas it stands for oxygen only. See #23.
        ## For compact-formulas(#10) there are many of them.
        % for f in one_terminal_formulas:
        assertFalse(formulaIsRemoved("${f.text}"));
        % endfor
    % endif
    % for final in True, False:
<%        comment_added = False %>\
        % for f in group:
            % if len(f) > 1 and (f.is_final() == final):
                % if not comment_added:
<%                  comment_added = True %>\

                    % if final:
        // Formulas that match limit conditions
                    % else:
        // Formulas that do not match limit conditions
                    % endif
                % endif
                % if f.prefix() != f.text:
        // ${f.text}
                % endif
        e = elem("${f.prefix()}");
<%                t = '"%s"' % '", "'.join(map(str, f.terms)) %>\
                % if f.is_final():
        assertTrue(e.atomCount() > 0);
        assertTrue(e.atomicWeight() > 0);
        assertTrue(mFormulaTerms.containsKey(e));
        assertArrayEquals(elements(${t}).toArray(), mFormulaTerms.get(e));
        assertTrue(reactionProduces(e, elements(${t})));
        assertTrue(formulaIsRemoved(${t}));
                % else:
        assertFalse(mFormulaTerms.containsKey(e));
        assertFalse(reactionProduces(e, elements(${t})));
                ## Do not assertFalse(formulaIsRemoved(${t})) because parts of
                ## a terminal sequence could match other formulas and
                ## the sequence can get validly removed.
                % endif

            % endif
        % endfor
    % endfor
    }
% endfor

    private boolean formulaIsRemoved(String... fieldMap) {
        Field field = new RectField(fieldMap.length, 1);
        for (int i = 0; i < fieldMap.length; ++i) {
            field.at(i).setElement(elem(fieldMap[i]));
        }
        removeCompounds(field);

        for (int i = 0; i < fieldMap.length; ++i) {
            if (!field.at(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void removeCompounds(Field field) {
        for (CompoundReference ref : mScanner.scan(field)) {
            field.removeCompound(ref.getCells());
        }
    }

    private boolean reactionProduces(Element e, ArrayList<Element> terms) {
        return mReactor.getCompounds(terms).contains(e);
    }

    private Element elem(String id) {
        return mRegistry.get(id);
    }

    private ArrayList<Element> elements(String... terms) {
        ArrayList<Element> r = new ArrayList<Element>(terms.length);
        for (String it : terms) {
            r.add(elem(it));
        }
        return r;
    }
}
## vim: set ft=mako :
