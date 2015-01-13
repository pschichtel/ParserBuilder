package de.cubeisland.engine.parser.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.cubeisland.engine.parser.TestGrammars;
import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import junit.framework.TestCase;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class BaseGrammarTest extends TestCase
{


    Grammar g = TestGrammars.SIMPLE_EXPR_WITH_EPS;
    Grammar h = TestGrammars.SIMPLE_EXPR;

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

    public void testFirst() throws Exception
    {
        // TODO add some fancy magic to the test
        System.out.println("test");
        for (Variable var : h.getVariables())
        {
            System.out.println(var + " --> " + h.first(2, var));
        }
        assertThat("dummy", true, is(true));
    }
}