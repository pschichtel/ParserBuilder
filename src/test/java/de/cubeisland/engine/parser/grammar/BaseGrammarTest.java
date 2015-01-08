package de.cubeisland.engine.parser.grammar;

import de.cubeisland.engine.parser.TestGrammars;
import de.cubeisland.engine.parser.Variable;
import junit.framework.TestCase;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class BaseGrammarTest extends TestCase
{


    Grammar g = TestGrammars.SIMPLE_EXPR_WITH_EPS;

    public void testIsNullable() throws Exception
    {
        System.out.println(g);

        for (Variable var : g.getVariables())
        {
            if (var.getName() == "expr")
            {
                assertThat("Variable expr is nullable.", g.isNullable(var), is(true));
            }
            else
            {
                assertThat("Variable " + var.getName() + " is not nullable.", g.isNullable(var), is(false));
            }
        }
    }
}