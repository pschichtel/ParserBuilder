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
package de.cubeisland.engine.parser.rule.token.automate;

import java.util.regex.Pattern;
import org.junit.Test;

public class MatcherTest
{

    protected static void printAutomate(String name, FiniteAutomate<? extends Transition> a)
    {
        System.out.println(name + ":");
        System.out.println("States:      " + a.getStates());
        System.out.println("Transitions: " + a.getTransitions());
        System.out.println("Accepting:   " + a.getAcceptingStates());
        System.out.println("Start:       " + a.getStartState());
        System.out.println();
    }

    @Test
    public void testRead() throws Exception
    {
        DFA a = Matcher.matchAll('a');

        printAutomate("Read char", a);
    }

    @Test
    public void testAnd() throws Exception
    {
        DFA a = Matcher.matchAll('a');
        DFA b = Matcher.matchAll('b');

        NFA c = a.and(b);
        printAutomate("And", c);
    }

    @Test
    public void testOr() throws Exception
    {
        DFA a = Matcher.matchAll('a');
        DFA b = Matcher.matchAll('b');

        NFA c = a.and(b);
        printAutomate("Or", c);
    }

    @Test
    public void testKleene() throws Exception
    {
        DFA a = Matcher.matchAll('a');

        NFA b = a.kleene();
        printAutomate("Kleene", b);
    }

    @Test
    public void testPattern() throws Exception
    {
        Pattern p = Pattern.compile("[ab]*");
        System.out.println("Pattern.toString(): " + p.toString());

        NFA aPlus = Matcher.match(p).toNFA();
        printAutomate(p.toString() + " NFA", aPlus);

        DFA aPlusD = aPlus.toDFA().minimize();
        printAutomate(p.toString() + " DFA", aPlusD);
    }
}
