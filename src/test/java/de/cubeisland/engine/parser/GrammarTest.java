package de.cubeisland.engine.parser;

import de.cubeisland.engine.parser.rule.token.TokenSpec;
import org.junit.Test;

import static de.cubeisland.engine.parser.AugmentedGrammar.AUGMENTED_START;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.simple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GrammarTest
{
    @Test
    public void testAugment() throws Exception
    {
        final Variable start = new Variable("start");
        final TokenSpec A = simple("a", "a");
        final TokenSpec B = simple("b", "b");
        Grammar g = Grammar
            .with(start, A)
            .and(start, B)
            .startingWith(start);

        AugmentedGrammar a = g.augment();

        System.out.println(g);
        System.out.println(a);

        assertThat("The new start symbol is not the singleton augmented start symbol", a.getStart(), is(AUGMENTED_START));
        assertThat("Variable count doesn't fit", a.getVariables().size(), is(g.getVariables().size() + 1));
        assertThat("Production count doesn't fit", a.getRules().size(), is(g.getRules().size() + 1));
    }
}
