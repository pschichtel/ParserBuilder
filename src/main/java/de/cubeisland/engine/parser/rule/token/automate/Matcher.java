package de.cubeisland.engine.parser.rule.token.automate;

import java.util.HashSet;
import java.util.Set;

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
}
