/**
 * The MIT License
 * Copyright (c) 2014 Cube Island
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.cubeisland.engine.parser.grammar;

import de.cubeisland.engine.parser.TestGrammars;
import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.util.TokenString;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static de.cubeisland.engine.parser.util.PrintingUtil.printTokenStringMap;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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

    @Test
    public void testFirst() throws Exception
    {
        // TODO add some fancy magic to the test
        final Map<Variable, Set<TokenString>> first = TestGrammars.FIRST_N_FOLLOW_TEST_GRAMMAR.first(1);

        printTokenStringMap(first);
    }

    @Test
    public void testFollow() throws Exception
    {
        // TODO add some fancy magic
        final Map<Variable, Set<TokenString>> follow = TestGrammars.FIRST_N_FOLLOW_TEST_GRAMMAR.follow(2);

        printTokenStringMap(follow);
    }
}
