package de.cubeisland.engine.parser.messagecompositor;

import de.cubeisland.engine.parser.grammar.Grammar;
import org.junit.Before;
import org.junit.Test;

import static de.cubeisland.engine.parser.util.PrintingUtil.printTokenStringMap;

public class MessageCompositorTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGrammar() {
        final Grammar g = MessageCompositorSpec.GRAMMAR;

        System.out.println(g.augment());
        printTokenStringMap(g.first());
        printTokenStringMap(g.follow());
    }
}
