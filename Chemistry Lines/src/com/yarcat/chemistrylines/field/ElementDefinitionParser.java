package com.yarcat.chemistrylines.field;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/** Parses elements definition, populates ElementRegistry. */
public class ElementDefinitionParser {

    private final ElementRegistry mRegistry;
    private final BufferedReader mStream;

    public ElementDefinitionParser(ElementRegistry registry, BufferedReader in) {
        this.mRegistry = registry;
        this.mStream = in;
    }

    public final static ElementRegistry parse(StringReader stream)
            throws IOException {
        ElementRegistry registry = new ElementRegistry();
        ElementDefinitionParser parser = new ElementDefinitionParser(registry,
                new BufferedReader(stream));
        parser.parseKnownElements();
        parser.parseProducts();
        return registry;
    }

    private void parseProducts() throws IOException {
        while (true) {
            String string = skipComments();
            if (string == null) {
                break;
            }
            if (string.length() > 0) {
                String[] strings = string.split("\\s+");
                mRegistry.addProduction(strings);
            }
        }
    }

    private void parseKnownElements() throws IOException {
        while (true) {
            String string = skipComments();
            assert string != null;
            if (string.length() == 0) {
                break;
            }
            String[] strings = string.split("\\s+", 2);
            assert strings.length == 2;
            String id = strings[0];
            String name = strings[1].trim();
            mRegistry.addElement(id, name);
        }
    }

    /** Skips comments, returns next non-comment string. */
    private final String skipComments() throws IOException {
        String string;
        while ((string = mStream.readLine()) != null) {
            string = string.trim();
            if (!isComment(string)) {
                break;
            }
        }
        return string;
    }

    /** Returns true if string is a comment. */
    private final boolean isComment(String string) {
        return string.startsWith("//");
    }
}
