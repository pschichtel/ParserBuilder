package de.cubeisland.engine.parser.rule.token.automate;

import java.util.*;

import static java.util.Collections.unmodifiableSet;

public abstract class FiniteAutomate<T extends Transition>
{
    private final Set<State> states;
    private final Set<T> transitions;
    private final Set<State> acceptingStates;
    private final State start;

    private final Map<State, TransitionMap> transitionLookup;

    protected FiniteAutomate(Set<State> states, Set<T> transitions, State start, Set<State> acceptingStates) {

        states.addAll(acceptingStates);
        states.add(start);

        this.states = unmodifiableSet(states);
        this.transitions = unmodifiableSet(transitions);
        this.acceptingStates = unmodifiableSet(acceptingStates);
        this.start = start;
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

    public Set<State> getStates()
    {
        return this.states;
    }

    public Set<T> getTransitions()
    {
        return this.transitions;
    }

    public Set<State> getAcceptingStates()
    {
        return this.acceptingStates;
    }

    public State getStartState()
    {
        return this.start;
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
        return lookup.getExpectedChars();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof FiniteAutomate))
        {
            return false;
        }

        FiniteAutomate that = (FiniteAutomate) o;

        if (!acceptingStates.equals(that.acceptingStates))
        {
            return false;
        }
        if (!start.equals(that.start))
        {
            return false;
        }
        if (!states.equals(that.states))
        {
            return false;
        }
        if (!transitions.equals(that.transitions))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = states.hashCode();
        result = 31 * result + transitions.hashCode();
        result = 31 * result + acceptingStates.hashCode();
        result = 31 * result + start.hashCode();
        return result;
    }

}
