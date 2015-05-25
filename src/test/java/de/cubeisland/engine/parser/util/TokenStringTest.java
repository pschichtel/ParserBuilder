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
package de.cubeisland.engine.parser.util;

import de.cubeisland.engine.parser.rule.token.SimpleTokenClass;
import junit.framework.TestCase;

import java.util.*;

import static de.cubeisland.engine.parser.util.TokenString.concatMany;
import static de.cubeisland.engine.parser.util.TokenString.str;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class TokenStringTest extends TestCase
{
    private final TokenString v;
    private final TokenString w;

    private SimpleTokenClass ta = new SimpleTokenClass("a");
    private SimpleTokenClass tb = new SimpleTokenClass("b");
    private SimpleTokenClass tc = new SimpleTokenClass("c");

    private SimpleTokenClass t1 = new SimpleTokenClass("1");
    private SimpleTokenClass t2 = new SimpleTokenClass("2");
    private SimpleTokenClass t3 = new SimpleTokenClass("3");

    private Set<TokenString> m = new HashSet<TokenString>();
    private Set<TokenString> n = new HashSet<TokenString>();
    private Set<TokenString> o = new HashSet<TokenString>();

    public TokenStringTest()
    {
        this.v = str(this.ta, this.tb, this.tc);
        this.w = str(this.t1, this.t2, this.t3);

        this.m.add(this.v);
        this.m.add(this.w);

        this.n.add(this.v);
        this.n.add(this.w);

        this.o.add(this.v);
    }

    public void testConcatPrefix() throws Exception
    {
        final TokenString solution = str(this.ta, this.tb, this.tc, this.t1, this.t2, this.t3);

        for (int i = 0; i < 10; i++)
        {
            assertThat("Concatenation of two lists with k = " + i + " failed.", this.v.concat(i, this.w), is(solution.maximumSubstring(0, i)));
        }
    }

    public void testConcatPrefix1() throws Exception
    {
        for (int i = 0; i < 10; i++)
        {
            Set<TokenString> solution = new HashSet<TokenString>();

            solution.add(this.v.concat(i, this.w));
            solution.add(this.v.concat(i, this.v));
            solution.add(this.w.concat(i, this.v));
            solution.add(this.w.concat(i, this.w));

            Set<TokenString> proposal = concatMany(i, this.m, this.n);

            assertThat("Concatenation of two sets with k = " + i + " failed.", proposal, is(solution));
        }
    }

    public void testConcatPrefix2() throws Exception
    {
        for (int i = 0; i < 10; i++)
        {
            Set<TokenString> solution = concatMany(i, concatMany(i, this.m, this.n), this.o);

            Set<TokenString> proposal = concatMany(i, this.m, this.n, this.o);

            assertThat("Concatenation of three sets with k = " + i + " failed.", proposal, is(solution));
        }
    }
}
