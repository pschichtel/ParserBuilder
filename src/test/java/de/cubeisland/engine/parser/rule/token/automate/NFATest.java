package de.cubeisland.engine.parser.rule.token.automate;

import de.cubeisland.engine.parser.Util;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static de.cubeisland.engine.parser.Util.asSet;

public class NFATest
{
    private NFA stroetiExample43;
    private NFA stroetiExample44;

    @Before
    public void setUp() throws Exception
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
        Set<Transition> transitions = asSet(
                new SpontaneousTransition(q0, q1),
                new SpontaneousTransition(q0, q2),
                new ExpectedTransition(q1, 'b', q3),
                new ExpectedTransition(q2, 'a', q4),
                new ExpectedTransition(q3, 'a', q5),
                new ExpectedTransition(q4, 'b', q6),
                new SpontaneousTransition(q5, q7),
                new SpontaneousTransition(q6, q7),
                new SpontaneousTransition(q7, q0)
        );

        this.stroetiExample44 = new NFA(states, transitions, q0, asSet(q7));

        states = asSet(q0, q1, q2, q3);
        transitions = Util.<Transition>asSet(
                new ExpectedTransition(q0, 'a', q0),
                new ExpectedTransition(q0, 'b', q0),
                new ExpectedTransition(q0, 'b', q1),
                new ExpectedTransition(q1, 'a', q2),
                new ExpectedTransition(q1, 'b', q2),
                new ExpectedTransition(q2, 'a', q3),
                new ExpectedTransition(q2, 'b', q3)
        );

        this.stroetiExample43 = new NFA(states, transitions, q0, asSet(q3));
    }

    @Test
    public void testClosure44() throws Exception
    {
        MatcherTest.printAutomate("Stroeti NFA example 4.4", stroetiExample44);

        for (State state : stroetiExample44.getStates())
        {
            System.out.println("ec(" + state + ") = " + stroetiExample44.epsilonClosure(asSet(state)));
        }
    }

    @Test
    public void testToDFA() throws Exception
    {
        MatcherTest.printAutomate("toDFA", stroetiExample44.toDFA());
    }

    @Test
    public void testToDFA2() throws Exception
    {
        MatcherTest.printAutomate("toDFA", stroetiExample43.toDFA());
    }
}