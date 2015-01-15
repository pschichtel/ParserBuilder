package de.cubeisland.engine.parser.rule.token.automate;

import java.util.HashSet;
import java.util.Set;

import static de.cubeisland.engine.parser.Util.asSet;

public class Read
{
    public static DFA read(char c)
    {
        State start = new State();
        State accept = new State();
        ExpectedTransition t = new ExpectedTransition(start, c, accept);

        return new DFA(asSet(start, accept), asSet(t), start, asSet(accept));
    }

    @SuppressWarnings("unchecked")
    public static NFA and(FiniteAutomate<? extends Transition> a, FiniteAutomate<? extends Transition> b)
    {
        final Set<State> states = states(a, b);
        final Set<Transition> transitions = transitions(a, b);

        for (State state : a.getAcceptingStates())
        {
            transitions.add(new SpontaneousTransition(state, b.getStartState()));
        }

        return new NFA(states, transitions, a.getStartState(), b.getAcceptingStates());
    }

    @SuppressWarnings("unchecked")
    public static NFA or(FiniteAutomate<? extends Transition> a, FiniteAutomate<? extends Transition> b)
    {
        final Set<State> states = states(a, b);
        final Set<Transition> transitions = transitions(a, b);

        final State start = new State();
        final State accept = new State();

        transitions.add(new SpontaneousTransition(start, a.getStartState()));
        transitions.add(new SpontaneousTransition(start, b.getStartState()));

        for (State state : a.getAcceptingStates())
        {
            transitions.add(new SpontaneousTransition(state, accept));
        }

        for (State state : b.getAcceptingStates())
        {
            transitions.add(new SpontaneousTransition(state, accept));
        }

        return new NFA(states, transitions, start, asSet(accept));
    }

    @SuppressWarnings("unchecked")
    public static NFA kleene(FiniteAutomate<? extends Transition> a)
    {
        final Set<State> states = states(a);
        final Set<Transition> transitions = transitions(a);

        final State start = new State();
        final State accept = new State();

        transitions.add(new SpontaneousTransition(start, accept));
        transitions.add(new SpontaneousTransition(start, a.getStartState()));
        for (State state : a.getAcceptingStates())
        {
            transitions.add(new SpontaneousTransition(state, a.getStartState()));
            transitions.add(new SpontaneousTransition(state, accept));
        }

        return new NFA(states, transitions, start, asSet(accept));
    }

    private static Set<State> states(FiniteAutomate<? extends Transition>... automates)
    {
        Set<State> states = new HashSet<State>();
        for (FiniteAutomate<? extends Transition> automate : automates)
        {
            states.addAll(automate.getStates());
        }
        return states;
    }

    private static Set<Transition> transitions(FiniteAutomate<? extends Transition>... automates)
    {
        Set<Transition> transitions = new HashSet<Transition>();
        for (FiniteAutomate<? extends Transition> automate : automates)
        {
            transitions.addAll(automate.getTransitions());
        }
        return transitions;
    }
}
