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

    private static final String FORMULA_FILTER_FILE_NAME =
        "formula-exceptions.txt";

    public static FormulaLinesGame formulaShuffleGame(Field field) {
        Set<String> exceptions = readExceptionSet(FORMULA_FILTER_FILE_NAME);
        Map<Element, Element[]> ft = KnownFormulas.formulaTerms;
        Map<Element, Element[]> filtered =
            exceptions == null ? ft : filterExceptions(ft, exceptions);
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

    public static Map<Element, Element[]> filterExceptions(
            Map<Element, Element[]> formulaTerms, Set<String> exceptions) {
        HashMap<Element, Element[]> r = new HashMap<Element, Element[]>();
        for (Entry<Element, Element[]> it : formulaTerms.entrySet()) {
            if (!exceptions.contains(it.getKey().getId())) {
                r.put(it.getKey(), it.getValue());
            }
        }
        return r;
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
