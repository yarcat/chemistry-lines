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

import org.junit.Before;
import org.junit.Test;

import com.yarcat.chemistrylines.algorithms.ChemicalReactor;
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

    @Before
    public void setUp() {
        mRegistry = KnownFormulas.contents;
        mReactor = new SimpleReactor(mRegistry);
        mScanner = new CompoundScanner(mReactor);
    }

% for key, group in R.split_formula_set(formulas):
<%    group = list(group) %>

    @Test
    public void test${key}() {
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
<%                t = '"%s"' % '", "'.join(map(str, f.terms)) %>\
                % if f.is_final():
        assertTrue(reactionProduces("${f.prefix()}", ${t}));
        assertTrue(formulaIsRemoved(${t}));
                % else:
        assertFalse(reactionProduces("${f.prefix()}", ${t}));
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
            field.at(i).setElement(mRegistry.get(fieldMap[i]));
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
        for (int[] cells : mScanner.scan(field)) {
            field.removeCompound(cells);
        }
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
