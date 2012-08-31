package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.ElementDefinitionParser;
import com.yarcat.chemistrylines.field.ElementRegistry;

public class ElementDefinitionParserTest {

    private final static String PAYLOAD =
            "// Define known elements.\n" +
            "O{2-} One atom\n" + "O2{0} One molecula\n" +
            "O3{0} One super molecula\n" +
            "\n" +
            "// Define known productions.\n" +
            "\n" +
            "O{2-} O{2-} O2{0}\n" +
            "\n" +
            "// Random comment." +
            "\n" +
            "O{2-} O2{0} O3{0}\n";

    @Test
    public void testLoad() throws IOException {
        StringReader stream = new StringReader(PAYLOAD);
        ElementRegistry registry = ElementDefinitionParser.parse(stream);

        assertNull(registry.get("?"));

        Element o = registry.get("O{2-}");
        assertNotNull(o);
        Element o2 = registry.get("O2{0}");
        assertNotNull(o2);
        Element o3 = registry.get("O3{0}");
        assertNotNull(o3);

        assertArrayEquals(new Element[] { o2 }, registry.getProductions(o, o));
        assertArrayEquals(new Element[] { o3 }, registry.getProductions(o, o2));
        assertArrayEquals(null, registry.getProductions(o2, o));
    }
}
