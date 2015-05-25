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

import de.cubeisland.engine.parser.Util;
import de.cubeisland.engine.parser.rule.token.automate.transition.CharacterTransition;
import de.cubeisland.engine.parser.rule.token.automate.transition.ExpectedTransition;
import de.cubeisland.engine.parser.rule.token.automate.transition.WildcardTransition;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static de.cubeisland.engine.parser.Util.asSet;
import static de.cubeisland.engine.parser.util.PrintingUtil.printAutomate;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DFATest
{
    @Test
    public void testGetBy() throws Exception
    {
        String string = "Test123";
        String string2 = "super";
        DFA a = Matcher.match(string).or(Matcher.match(string2)).toDFA();

        State s = a.getStartState();
        for (char c : string.toCharArray())
        {
            System.out.println("Current state: " + s);
            s = s.transition(a, c);
        }

        assertThat("String was not matched!", a.isAccepting(s), is(true));

        s = a.getStartState();
        for (char c : string2.toCharArray())
        {
            System.out.println("Current state: " + s);
            s = s.transition(a, c);
        }

        assertThat("String2 was not matched!", a.isAccepting(s), is(true));
    }

    @Test
    public void testMinimize() throws Exception
    {
        State q0 = new NamedState("q0");
        State q1 = new NamedState("q1");
        State q2 = new NamedState("q2");
        State q3 = new NamedState("q3");
        State q4 = new NamedState("q4");
        State q5 = new NamedState("q5");
        State q6 = new NamedState("q6");
        State q7 = new NamedState("q7");

        Set<State> states = asSet(q0, q1, q2, q3, q4, q5, q6, q7);
        Set<ExpectedTransition> transitions = Util.<ExpectedTransition>asSet(new CharacterTransition(q0, 'a', q1),
                                                                             new CharacterTransition(q0, 'b', q2),
                                                                             new CharacterTransition(q1, 'a', q3),
                                                                             new CharacterTransition(q1, 'b', q3),
                                                                             new CharacterTransition(q2, 'a', q4),
                                                                             new CharacterTransition(q2, 'b', q4),
                                                                             new CharacterTransition(q3, 'a', q1),
                                                                             new CharacterTransition(q3, 'b', q1),
                                                                             new CharacterTransition(q4, 'a', q2),
                                                                             new CharacterTransition(q4, 'b', q2),
                                                                             new CharacterTransition(q5, 'c', q6),
                                                                             new CharacterTransition(q6, 'd', q7));

        DFA stroeti51 = new DFA(states, transitions, q0, asSet(q3, q4));

        printAutomate("not minimized", stroeti51);

        DFA minimized = stroeti51.minimize();
        printAutomate("minimized", minimized);

        assertThat("Start states not equal", minimized.getStartState(), is(stroeti51.getStartState()));
        assertThat("Unexpected number of states", minimized.getStates().size(), is(3));
    }

    @Test
    public void testComplement() throws Exception
    {
        final State s1 = new State();
        final State s2 = new State();
        final char acceptedChar = 'a';
        final char rejectedChar = 'r';

        final ExpectedTransition t = new CharacterTransition(s1, acceptedChar, s2);

        final DFA a = new DFA(asSet(s1, s2), asSet(t), s1, asSet(s2));
        final DFA aComplement = a.complement();
        final DFA aAgain = aComplement.complement();

        printAutomate("base a", a);
        printAutomate("complement a", aComplement);
        printAutomate("complement complement a", aAgain);

        assertTrue("accepted char was accepted by complement",
                   a.isAccepting(a.getStartState().transition(a, acceptedChar)) != aComplement.isAccepting(aComplement.getStartState().transition(aComplement, acceptedChar)));
        assertTrue("rejected char was not accepted by complement",
                   a.isAccepting(a.getStartState().transition(a, rejectedChar)) != aComplement.isAccepting(aComplement.getStartState().transition(aComplement, rejectedChar)));
        assertTrue("accepted char was accepted by complement of complement of a",
                   a.isAccepting(a.getStartState().transition(a, acceptedChar)) == aAgain.isAccepting(aAgain.getStartState().transition(aAgain, acceptedChar)));
        assertTrue("rejected char was not accepted by complement of complement of a",
                   a.isAccepting(a.getStartState().transition(a, rejectedChar)) == aAgain.isAccepting(aAgain.getStartState().transition(aAgain, rejectedChar)));
    }

    @Test
    public void testMinimizeWithWildcard() throws Exception
    {
        State s0 = new State();
        State s1 = new State();
        State s2 = new State();
        final Set<State> states = asSet(s0, s1, s2);

        Set<ExpectedTransition> transitions = new HashSet<ExpectedTransition>();
        transitions.add(new WildcardTransition(s0, s1));
        transitions.add(new WildcardTransition(s1, s2));
        transitions.add(new WildcardTransition(s2, s0));
//        transitions.add(new CharacterTransition(s0, 'a', s1));
//        transitions.add(new CharacterTransition(s1, 'a', s2));
//        transitions.add(new CharacterTransition(s2, 'a', s0));

        DFA a = new DFA(states, transitions, s0, states);
        DFA aMin = a.minimize();

        printAutomate("a unminimized", a);
        printAutomate("a minimized", aMin);
    }
}
