package de.cubeisland.engine.parser.rule.token.automate;

import java.util.*;

import static de.cubeisland.engine.parser.Util.asSet;
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

    @SuppressWarnings("unchecked")
    public NFA and(FiniteAutomate<? extends Transition> other)
    {
        final Set<State> states = mergeStates(this, other);
        final Set<Transition> transitions = mergeTransitions(this, other);

        for (State state : this.getAcceptingStates())
        {
            transitions.add(new SpontaneousTransition(state, other.getStartState()));
        }

        return new NFA(states, transitions, this.getStartState(), other.getAcceptingStates());
    }

    @SuppressWarnings("unchecked")
    public NFA or(FiniteAutomate<? extends Transition> other)
    {
        final Set<State> states = mergeStates(this, other);
        final Set<Transition> transitions = mergeTransitions(this, other);

        final State start = new State();
        final State accept = new State();

        transitions.add(new SpontaneousTransition(start, this.getStartState()));
        transitions.add(new SpontaneousTransition(start, other.getStartState()));

        for (State state : this.getAcceptingStates())
        {
            transitions.add(new SpontaneousTransition(state, accept));
        }

        for (State state : other.getAcceptingStates())
        {
            transitions.add(new SpontaneousTransition(state, accept));
        }

        return new NFA(states, transitions, start, asSet(accept));
    }

    public NFA kleene()
    {
        final Set<State> states = new HashSet<State>(getStates());
        final Set<Transition> transitions = new HashSet<Transition>(getTransitions());

        final State start = new State();
        final State accept = new State();

        transitions.add(new SpontaneousTransition(start, accept));
        transitions.add(new SpontaneousTransition(start, getStartState()));
        for (State state : getAcceptingStates())
        {
            transitions.add(new SpontaneousTransition(state, getStartState()));
            transitions.add(new SpontaneousTransition(state, accept));
        }

        return new NFA(states, transitions, start, asSet(accept));
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

    protected static Set<State> mergeStates(FiniteAutomate<? extends Transition>... automates)
    {
        Set<State> states = new HashSet<State>();
        for (FiniteAutomate<? extends Transition> automate : automates)
        {
            states.addAll(automate.getStates());
        }
        return states;
    }

    protected static Set<Transition> mergeTransitions(FiniteAutomate<? extends Transition>... automates)
    {
        Set<Transition> transitions = new HashSet<Transition>();
        for (FiniteAutomate<? extends Transition> automate : automates)
        {
            transitions.addAll(automate.getTransitions());
        }
        return transitions;
    }
}
