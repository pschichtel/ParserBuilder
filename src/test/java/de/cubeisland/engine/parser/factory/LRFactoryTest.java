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
package de.cubeisland.engine.parser.factory;

import de.cubeisland.engine.parser.ParseState;
import de.cubeisland.engine.parser.TestGrammars;
import de.cubeisland.engine.parser.TestGrammars.SimpleExpr;
import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.Rule.MarkedRule;
import org.junit.Test;

import java.util.Set;

import static de.cubeisland.engine.parser.Util.asSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LRFactoryTest
{
    Grammar g = TestGrammars.SIMPLE_EXPR;
    AugmentedGrammar a = g.augment();
    LRFactory lr = new LRFactory();

    @Test
    public void testClosure() throws Exception
    {
        System.out.println(a);
        Set<MarkedRule> markedRules = asSet(a.getStartRule().mark());
        System.out.println(markedRules);
        Set<MarkedRule> closure = lr.closure(a, markedRules);
        System.out.println(closure);

        assertThat("LRType.closure() did not find all productions", closure.size(), is(9));
    }

    @Test
    public void testReadElement() throws Exception
    {
        ParseState initial = lr.calculateInitialState(a);
        ParseState newState = lr.goTo(a, initial, SimpleExpr.BGN);
        System.out.println(newState);

        assertThat("LRType.goTo() did not find all productions", newState.getRules().size(), is(9));
    }
}
