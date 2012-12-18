package com.yarcat.chemistrylines.game;

import static com.yarcat.chemistrylines.constants.PORTION_SIZE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.KnownFormulas;

public class FormulaShuffleFactory {

    @SuppressWarnings("serial")
    public static class NoFormulas extends Exception {
        @Override
        public String getMessage() {
            return "All formulas were filtered out.\n"
                + "There is nothing to play with.\n\nSorry :(";
        }
    }

    private static final String TERMINAL_FILTER_FILE_NAME =
        "element-exceptions.txt";
    private static final String FORMULA_FILTER_FILE_NAME =
        "formula-exceptions.txt";

    public static FormulaLinesGame formulaShuffleGame(Field field)
            throws NoFormulas {
        Map<Element, Element[]> ft = KnownFormulas.formulaTerms;
        Map<Element, Element[]> filtered =
            filterByTerminals(
                filterByFormulas(ft, readExceptionSet(FORMULA_FILTER_FILE_NAME)),
                readExceptionSet(TERMINAL_FILTER_FILE_NAME));
        if (filtered.isEmpty()) {
            throw new NoFormulas();
        }
        Element[][] formulas = valuesArray(filtered.values());
        // @formatter:off
        return new FormulaLinesGame(field,
            new FormulaLinesGame.FormulaTerminalGenerator(formulas, PORTION_SIZE));
        // @formatter:on
    }

    static Element[][] valuesArray(Collection<Element[]> values) {
        Element[][] r = new Element[values.size()][];
        int i = 0;
        for (Element[] it : values) {
            r[i++] = it;
        }
        return r;
    }

    public static Map<Element, Element[]> filterByFormulas(
            Map<Element, Element[]> formulaTerms, Set<String> exceptions) {
        Map<Element, Element[]> r;
        if (exceptions == null) {
            r = formulaTerms;
        } else {
            r = new HashMap<Element, Element[]>();
            for (Entry<Element, Element[]> it : formulaTerms.entrySet()) {
                if (!formulaMatchesException(it.getKey(), exceptions)) {
                    r.put(it.getKey(), it.getValue());
                }
            }
        }
        return r;
    }

    private static boolean formulaMatchesException(Element e,
            Set<String> exceptions) {
        return exceptions.contains(e.getId());
    }

    public static Map<Element, Element[]> filterByTerminals(
            Map<Element, Element[]> formulaTerms, Set<String> exceptions) {
        Map<Element, Element[]> r;
        if (exceptions == null) {
            r = formulaTerms;
        } else {
            r = new HashMap<Element, Element[]>();
            for (Entry<Element, Element[]> it : formulaTerms.entrySet()) {
                if (termsMatchException(it.getValue(), exceptions)) {
                    r.put(it.getKey(), it.getValue());
                }
            }
        }
        return r;
    }

    private static boolean termsMatchException(Element[] terms,
            Set<String> exceptions) {
        int i = 0;
        while (i < terms.length && !exceptions.contains(terms[i].getId())) {
            ++i;
        }
        return i == terms.length;
    }

    private static Set<String> readExceptionSet(String fileName) {
        Set<String> r;
        try {
            r = new HashSet<String>();
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String id;
            while ((id = br.readLine()) != null) {
                r.add(id.replaceAll("\\s", ""));
            }
            br.close();
        } catch (IOException e) {
            // Let there be no exception on IO-error.
            r = null;
        }
        return r;
    }
}
