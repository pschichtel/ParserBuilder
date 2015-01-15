package de.cubeisland.engine.parser.rule.token.automate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static de.cubeisland.engine.parser.rule.token.automate.ErrorState.ERROR;

public class DFA extends FiniteAutomate<ExpectedTransition>
{
    private final Map<State, Map<Character, ExpectedTransition>> transitionLookup;

    public DFA(Set<State> states, Set<ExpectedTransition> transitions, State start, Set<State> acceptingStates)
    {
        super(states, transitions, start, acceptingStates);
        this.transitionLookup = calculateTransitionLookup(transitions);
    }

    private static Map<State, Map<Character, ExpectedTransition>> calculateTransitionLookup(Set<ExpectedTransition> transitions)
    {
        final Map<State, Map<Character, ExpectedTransition>> transitionLookup = new HashMap<State, Map<Character, ExpectedTransition>>();

        for (ExpectedTransition transition : transitions)
        {
            Map<Character, ExpectedTransition> transitionMap = transitionLookup.get(transition.getOrigin());
            if (transitionMap == null)
            {
                transitionMap = new HashMap<Character, ExpectedTransition>();
                transitionLookup.put(transition.getOrigin(), transitionMap);
            }
            transitionMap.put(transition.getWith(), transition);
        }

        return transitionLookup;
    }

    public ExpectedTransition getTransitionFor(State s, char c)
    {
        Map<Character, ExpectedTransition> transitionMap = this.transitionLookup.get(s);
        if (transitionMap == null)
        {
            return null;
        }
        return transitionMap.get(c);
    }

    public State getBy(State s, char c)
    {
        Transition t = getTransitionFor(s, c);
        if (t == null)
        {
            return ERROR;
        }
        return t.getDestination();
    }

    public DFA minimize()
    {
        // TODO implement me
        throw new UnsupportedOperationException("implement me");
    }
}
