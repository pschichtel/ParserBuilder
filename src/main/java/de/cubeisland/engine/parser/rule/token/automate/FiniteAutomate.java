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

    private final Set<Character> alphabet;

    protected FiniteAutomate(Set<State> states, Set<T> transitions, State start, Set<State> acceptingStates) {

        states.addAll(acceptingStates);
        states.add(start);

        this.states = unmodifiableSet(states);
        this.transitions = unmodifiableSet(transitions);
        this.acceptingStates = unmodifiableSet(acceptingStates);
        this.start = start;
        this.alphabet = unmodifiableSet(calculateAlphabet(transitions));

    }

    private static Set<Character> calculateAlphabet(Set<? extends Transition> transitions)
    {
        Set<Character> chars = new HashSet<Character>();

        for (Transition transition : transitions)
        {
            if (transition instanceof ExpectedTransition)
            {
                chars.add(((ExpectedTransition) transition).getWith());
            }
        }

        return chars;
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

    public Set<Character> getAlphabet()
    {
        return this.alphabet;
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
