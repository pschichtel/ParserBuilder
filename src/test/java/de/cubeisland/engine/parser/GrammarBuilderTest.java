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
package de.cubeisland.engine.parser;

import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.TokenClass;
import org.junit.Test;

import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.simple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GrammarBuilderTest
{
    @Test
    public void testBuild() throws Exception
    {
        final Variable start = new Variable("start");
        final TokenClass A = simple("a");
        final TokenClass B = simple("b");
        Grammar g = Grammar
            .with(start.produces(A).skip())
            .with(start.produces(B).skip())
            .startingWith(start);

        System.out.println(g);

        assertThat("Number of rules does not equal defined rules", g.getRules().size(), is(2));
        assertThat("Number of variables does not equal defined variable", g.getVariables().size(), is(1));
        assertThat("Number of tokens does not equal defined tokens", g.getTokens().size(), is(2));
    }
}
