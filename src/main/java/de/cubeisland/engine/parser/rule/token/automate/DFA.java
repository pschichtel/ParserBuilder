package de.cubeisland.engine.parser.rule.token.automate;

import java.util.Set;

public class DFA extends FiniteAutomate<ExpectedTransition>
{
    public DFA(Set<State> states, Set<ExpectedTransition> transitions, State start, Set<State> acceptingStates)
    {
        super(states, transitions, start, acceptingStates);
    }

    public DFA optimize()
    {
        // TODO implement me
        throw new UnsupportedOperationException("implement me");
    }
}
