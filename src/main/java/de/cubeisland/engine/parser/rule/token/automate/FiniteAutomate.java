/**
 * The MIT License
 * Copyright (c) 2014 Cube Island
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.cubeisland.engine.parser.rule.token.automate;

import de.cubeisland.engine.parser.util.FixPoint;
import de.cubeisland.engine.parser.util.Function;
import de.cubeisland.engine.parser.util.OrderedPair;

import java.util.HashSet;
import java.util.Set;

import static de.cubeisland.engine.parser.Util.asSet;
import static java.util.Collections.disjoint;
import static java.util.Collections.unmodifiableSet;

public abstract class FiniteAutomate<T extends Transition>
{
    private final Set<State> states;
    private final Set<T> transitions;
    private final Set<State> acceptingStates;
    private final State start;

    private final Set<Character> alphabet;
    private final Set<State> reachableStates;

    protected FiniteAutomate(Set<State> states, Set<T> transitions, State start, Set<State> acceptingStates) {

        states.addAll(acceptingStates);
        states.add(start);

        this.states = unmodifiableSet(states);
        this.transitions = unmodifiableSet(transitions);
        this.acceptingStates = unmodifiableSet(acceptingStates);
        this.start = start;
        this.alphabet = unmodifiableSet(calculateAlphabet(transitions));
        this.reachableStates = unmodifiableSet(findReachableStates());

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

    private Set<State> findReachableStates()
    {
        return FixPoint.apply(asSet(getStartState()), new Function<State, Set<State>>()
        {
            @Override
            public Set<State> apply(State in)
            {
                Set<State> out = new HashSet<State>();

                for (T t : getTransitions())
                {
                    if (t.getOrigin() == in)
                    {
                        out.add(t.getDestination());
                    }
                }

                return out;
            }
        });
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

    public boolean isAccepting(State s)
    {
        return getAcceptingStates().contains(s);
    }

    public Set<State> getReachableStates()
    {
        return this.reachableStates;
    }

    protected Set<State> complementaryAcceptingStates()
    {
        Set<State> states = new HashSet<State>(getStates());
        states.removeAll(getAcceptingStates());
        return states;
    }

    public abstract FiniteAutomate<? extends Transition> complement();

    public abstract DFA toDFA();

    public boolean isEmpty()
    {
        return disjoint(getReachableStates(), getAcceptingStates());
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

        // TODO implement me properly
        return false;
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
