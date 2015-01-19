package de.cubeisland.engine.parser.rule.token.automate;

import de.cubeisland.engine.parser.util.FixPoint;
import de.cubeisland.engine.parser.util.Function;

import java.util.*;

import static de.cubeisland.engine.parser.Util.asSet;

public class NFA extends FiniteAutomate<Transition>
{
    private final Map<State, TransitionMap> transitionLookup;

    public NFA(Set<State> states, Set<Transition> transitions, State start, Set<State> acceptingStates)
    {
        super(states, transitions, start, acceptingStates);
        this.transitionLookup = calculateTransitionLookup(transitions);
    }

    private static <T extends Transition> Map<State, TransitionMap> calculateTransitionLookup(Set<T> transitions)
    {
        Map<State, TransitionMap> transitionLookup = new HashMap<State, TransitionMap>();

        Map<State, Set<T>> stateTransitions = new HashMap<State, Set<T>>();
        for (T transition : transitions)
        {
            Set<T> t = stateTransitions.get(transition.getOrigin());
            if (t == null)
            {
                t = new HashSet<T>();
                stateTransitions.put(transition.getOrigin(), t);
            }
            t.add(transition);
        }

        for (Map.Entry<State, Set<T>> entry : stateTransitions.entrySet())
        {
            Map<Character, Set<ExpectedTransition>> expectedTransitions = new HashMap<Character, Set<ExpectedTransition>>();
            Set<SpontaneousTransition> spontaneousTransitions = new HashSet<SpontaneousTransition>();
            Set<Character> expectedChars = new HashSet<Character>();

            for (T t : entry.getValue())
            {
                if (t instanceof SpontaneousTransition)
                {
                    spontaneousTransitions.add((SpontaneousTransition) t);
                }
                else if (t instanceof ExpectedTransition)
                {
                    ExpectedTransition et = (ExpectedTransition) t;
                    Set<ExpectedTransition> expected = expectedTransitions.get(et.getWith());
                    if (expected == null)
                    {
                        expected = new HashSet<ExpectedTransition>();
                        expectedTransitions.put(et.getWith(), expected);
                    }
                    expected.add(et);
                    expectedChars.add(et.getWith());
                }
                else
                {
                    throw new IllegalArgumentException("Unknown transition type!");
                }
            }
            transitionLookup.put(entry.getKey(), new TransitionMap(expectedTransitions, spontaneousTransitions, expectedChars));
        }

        return transitionLookup;
    }

    public Set<SpontaneousTransition> getSpontaneousTransitionsFor(State s)
    {
        TransitionMap lookup = this.transitionLookup.get(s);
        if (lookup == null)
        {
            return Collections.emptySet();
        }
        return lookup.getSpontaneousTransitions();
    }

    public Set<ExpectedTransition> getExpectedTransitionsFor(State s, char c)
    {
        TransitionMap lookup = this.transitionLookup.get(s);
        if (lookup == null)
        {
            return Collections.emptySet();
        }
        return lookup.getTransitionsFor(c);
    }

    public Set<Character> getExpectedCharsFor(State s)
    {
        TransitionMap lookup = this.transitionLookup.get(s);
        if (lookup == null)
        {
            return Collections.emptySet();
        }
        return lookup.getAlphabet();
    }

    protected Set<State> epsilonClosure(Set<State> states)
    {
        return FixPoint.apply(states, new Function<State, Set<State>>()
        {
            @Override
            public Set<State> apply(State in)
            {
                Set<State> states = new HashSet<State>();
                for (SpontaneousTransition transition : getSpontaneousTransitionsFor(in))
                {
                    states.add(transition.getDestination());
                }
                return states;
            }
        });
    }

    private Set<Character> alphabetFor(Set<State> states)
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
        final Set<State> states = new HashSet<State>();
        final Set<ExpectedTransition> transitions = new HashSet<ExpectedTransition>();
        final State start = getStartState();
        final Set<State> accepting = new HashSet<State>();


        Map<Set<State>, State> knownStates = new HashMap<Set<State>, State>();
        Stack<State> checkStates = new Stack<State>();
        Stack<Set<State>> checkStateSets = new Stack<Set<State>>();


        Set<State> initialClosure = epsilonClosure(asSet(start));
        System.out.println("1. " + start + " = ec(" + start + ") = " + initialClosure);
        checkStates.push(start);
        checkStateSets.push(initialClosure);
        knownStates.put(initialClosure, start);
        for (State acceptingState : getAcceptingStates())
        {
            if (initialClosure.contains(acceptingState))
            {
                accepting.add(start);
                break;
            }
        }

        int i = 1;
        while (!checkStates.empty())
        {
            final State state = checkStates.pop();
            final Set<State> stateSet = checkStateSets.pop();
            for (char c : alphabetFor(stateSet))
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
                    knownStates.put(newStateSet, newState);
                    transitions.add(new ExpectedTransition(state, c, newState));
                    System.out.println(" = " + newState);

                    for (State acceptingState : getAcceptingStates())
                    {
                        if (newStateSet.contains(acceptingState))
                        {
                            accepting.add(newState);
                            break;
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

        System.out.println("Transitions: " + transitions.size());

        return new DFA(states, transitions, start, accepting);
    }
}
