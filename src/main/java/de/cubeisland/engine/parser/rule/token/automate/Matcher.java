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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static de.cubeisland.engine.parser.Util.asSet;

public abstract class Matcher
{
    private Matcher()
    {
    }

    public static DFA match(String s)
    {
        return match(s.toCharArray());
    }

    public static DFA match(char... chars)
    {
        Set<State> states = new HashSet<State>();
        Set<ExpectedTransition> transitions = new HashSet<ExpectedTransition>();
        State start = new State();

        State lastState = start;

        for (char c : chars)
        {
            State state = new State();
            states.add(state);
            transitions.add(new ExpectedTransition(lastState, c, state));
            lastState = state;
        }

        return new DFA(states, transitions, start, asSet(lastState));
    }

    public static FiniteAutomate<? extends Transition> match(Pattern pattern)
    {
        // TODO implement me
        return null;
    }
}
