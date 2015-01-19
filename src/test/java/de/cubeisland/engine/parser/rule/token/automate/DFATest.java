package de.cubeisland.engine.parser.rule.token.automate;

import org.junit.Test;

import java.util.Set;

import static de.cubeisland.engine.parser.Util.asSet;
import static de.cubeisland.engine.parser.rule.token.automate.Matcher.match;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DFATest
{
    @Test
    public void testGetBy() throws Exception
    {
        String string  = "Test123";
        String string2 = "super";
        DFA a = match(string).or(match(string2)).toDFA();

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

        Set<State> states = asSet(q0, q1, q2, q3, q4);
        Set<ExpectedTransition> transitions = asSet(
                new ExpectedTransition(q0, 'a', q1),
                new ExpectedTransition(q0, 'b', q2),
                new ExpectedTransition(q1, 'a', q3),
                new ExpectedTransition(q1, 'b', q3),
                new ExpectedTransition(q2, 'a', q4),
                new ExpectedTransition(q2, 'b', q4),
                new ExpectedTransition(q3, 'a', q1),
                new ExpectedTransition(q3, 'b', q1),
                new ExpectedTransition(q4, 'a', q2),
                new ExpectedTransition(q4, 'b', q2)
        );

        DFA stroeti51 = new DFA(states, transitions, q0, asSet(q3, q4));

        MatcherTest.printAutomate("not minimized", stroeti51);

        DFA minimized = stroeti51.minimize();
        MatcherTest.printAutomate("minimized", minimized);

        assertThat("Start states not equal", minimized.getStartState(), is(stroeti51.getStartState()));
        assertThat("Unexpected number of states", minimized.getStates().size(), is(3));
    }
}