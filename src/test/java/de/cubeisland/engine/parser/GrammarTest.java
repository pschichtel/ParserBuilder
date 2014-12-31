package de.cubeisland.engine.parser;

import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import org.junit.Test;

import static de.cubeisland.engine.parser.grammar.AugmentedGrammar.AUGMENTED_START;
import static de.cubeisland.engine.parser.rule.Rule.head;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.simple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GrammarTest
{
    @Test
    public void testAugment() throws Exception
    {
        Grammar g = TestGrammars.SIMPLE_EXPR;
        AugmentedGrammar a = g.augment();

        System.out.println(g);
        System.out.println(a);

        assertThat("The new start symbol is not the singleton augmented start symbol", a.getStart(), is(AUGMENTED_START));
        assertThat("Variable count doesn't fit", a.getVariables().size(), is(g.getVariables().size() + 1));
        assertThat("Production count doesn't fit", a.getRules().size(), is(g.getRules().size() + 1));
    }
}
