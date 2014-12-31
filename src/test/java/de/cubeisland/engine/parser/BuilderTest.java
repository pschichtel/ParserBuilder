package de.cubeisland.engine.parser;

import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenSpec;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import org.junit.Test;

import static de.cubeisland.engine.parser.rule.Rule.head;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.parametrized;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.simple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BuilderTest
{
    @Test
    public void testBuild() throws Exception
    {
        final Variable start = new Variable("start");
        final TokenSpec A = simple("a", "a");
        final TokenSpec B = simple("b", "b");
        Grammar g = Grammar
            .with(head(start).produces(A).skip())
            .with(head(start).produces(B).skip())
            .startingWith(start);

        System.out.println(g);

        assertThat("Number of rules does not equal defined rules", g.getRules().size(), is(8));
        assertThat("Number of variables does not equal defined variable", g.getVariables().size(), is(3));
        assertThat("Number of tokens does not equal defined tokens", g.getTokens().size(), is(7));
    }
}
