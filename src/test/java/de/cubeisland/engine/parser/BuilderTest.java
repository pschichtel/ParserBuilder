package de.cubeisland.engine.parser;

import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenSpec;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import org.junit.Test;

import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.parametrized;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.simple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BuilderTest
{
    @Test
    public void testBuild() throws Exception
    {
        final Variable product = new Variable("product");
        final Variable factor = new Variable("factor");
        final Variable expr = new Variable("expr");

        final TokenSpec ADD = simple("ADD", "+");
        final TokenSpec SUB = simple("SUB", "-");
        final TokenSpec MUL = simple("MUL", "*");
        final TokenSpec DIV = simple("DIV", "/");
        final TokenSpec BGN = simple("BGN", "(");
        final TokenSpec END = simple("END", ")");
        final ParametrizedTokenSpec<Integer> NUM = parametrized("NUM", "0|[1-9][0-9]*", Integer.class);

        Grammar g = Grammar
            .with(expr, expr, ADD, product)
            .and(expr, expr, SUB, product)
            .and(expr, product)
            .and(product, product, MUL, factor)
            .and(product, product, DIV, factor)
            .and(product, factor)
            .and(factor, BGN, expr, END)
            .and(factor, NUM)
            .startingWith(expr);

        System.out.println(g);

        assertThat("Number of rules does not equal defined rules", g.getRules().size(), is(8));
        assertThat("Number of variables does not equal defined variable", g.getVariables().size(), is(3));
        assertThat("Number of tokens does not equal defined tokens", g.getTokens().size(), is(7));
    }
}
