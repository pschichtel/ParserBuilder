package de.cubeisland.engine.parser.rule.token.automate;

import de.cubeisland.engine.parser.util.FixPoint;
import de.cubeisland.engine.parser.util.SetMapper;

import java.util.*;

import static de.cubeisland.engine.parser.Util.asSet;

public class NFA extends FiniteAutomate<Transition>
{
    public NFA(Set<State> states, Set<Transition> transitions, State start, Set<State> acceptingStates)
    {
        super(states, transitions, start, acceptingStates);
    }

    protected Set<State> epsilonClosure(Set<State> states)
    {
        return FixPoint.apply(states, new SetMapper<State>()
        {
            @Override
            public Set<State> apply(Set<State> in)
            {
                Set<State> states = new HashSet<State>();
                for (State state : in)
                {
                    for (SpontaneousTransition transition : getSpontaneousTransitionsFor(state))
                    {
                        states.add(transition.getDestination());
                    }
                }
                return states;
            }
        });
    }

    private Set<Character> expectedInputs(Set<State> states)
    {
        Set<Character> chars = new HashSet<Character>();

        for (State state : states)
        {
            chars.addAll(getExpectedCharsFor(state));
        }

        return chars;
    }

    private Set<Character> expectedCharsFor(Set<State> states)
    {
        Set<Character> chars = new HashSet<Character>();

        for (State state : states)
        {
            chars.addAll(getExpectedCharsFor(state));
        }

        return chars;
    }

    private Set<State> read(Set<State> states, char c)
    {
        Set<State> out = new HashSet<State>();

        for (State state : states)
        {
            for (ExpectedTransition transition : getExpectedTransitionsFor(state, c))
            {
                out.add(transition.getDestination());
            }
        }

        return out;
    }

    public DFA toDFA()
    {
        class DFAState
        {
            public final State state;
            public final Set<State> set;

            public DFAState(State state, Set<State> set)
            {
                this.state = state;
                this.set = set;
            }
        }

        final Set<State> states = new HashSet<State>();
        final Set<ExpectedTransition> transitions = new HashSet<ExpectedTransition>();
        final State start = getStartState();
        final Set<State> accepting = new HashSet<State>();


        Map<Set<State>, State> knownStates = new HashMap<Set<State>, State>();
        Stack<State> checkStates = new Stack<State>();
        Stack<Set<State>> checkStateSets = new Stack<Set<State>>();


        Set<State> initialClosure = epsilonClosure(asSet(start));
        System.out.println("1. " + start + " = ec(" + start + ") = " + initialClosure);
        knownStates.put(initialClosure, start);
        checkStates.push(start);
        checkStateSets.push(initialClosure);

        int i = 1;
        do
        {
            final State state = checkStates.pop();
            final Set<State> stateSet = checkStateSets.pop();
            for (char c : expectedCharsFor(stateSet))
            {
                Set<State> newStateSet = epsilonClosure(read(stateSet, c));
                System.out.print((++i) + ". Δ(" + state + ", " + c + ") = " + newStateSet);

                State alreadyKnown = knownStates.get(newStateSet);
                if (alreadyKnown == null)
                {
                    State newState = new State();
                    states.add(newState);
                    checkStates.push(newState);
                    checkStateSets.push(newStateSet);
                    knownStates.put(newStateSet, state);
                    transitions.add(new ExpectedTransition(state, c, newState));
                    System.out.println(" = " + newState);

                    for (State acceptingState : getAcceptingStates())
                    {
                        if (newStateSet.contains(acceptingState))
                        {
                            accepting.add(newState);
                        }
                    }
                }
                else
                {
                    transitions.add(new ExpectedTransition(state, c, alreadyKnown));
                    System.out.println(" = " + alreadyKnown);
                }
            }
        }
        while (!checkStates.empty());

        System.out.println("Transitions: " + transitions.size());

        return new DFA(states, transitions, start, accepting);
    }
}
