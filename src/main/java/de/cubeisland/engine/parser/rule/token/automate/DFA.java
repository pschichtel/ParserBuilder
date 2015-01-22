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

import de.cubeisland.engine.parser.util.UnorderedPair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

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

    @Override
    public DFA toDFA()
    {
        return this;
    }

    public DFA minimize()
    {
        final Set<State> states = getReachableStates();
        final Set<ExpectedTransition> transitions = new CopyOnWriteArraySet<ExpectedTransition>(getTransitions());
        State start = getStartState();
        final Set<State> accepting = new HashSet<State>(getAcceptingStates());


        Set<UnorderedPair<State, State>> statePairs = new HashSet<UnorderedPair<State, State>>();

        for (State p : states)
        {
            for (State q : states)
            {
                if (p != q)
                {
                    statePairs.add(new UnorderedPair<State, State>(p, q));
                }
            }
        }

        Set<UnorderedPair> separableStates = new HashSet<UnorderedPair>();
        for (UnorderedPair<State, State> p : statePairs)
        {
            if (isAccepting(p.getLeft()) && !isAccepting(p.getRight()) || !isAccepting(p.getLeft()) && isAccepting(p.getRight()))
            {
                separableStates.add(p);
            }
        }

        boolean changed;
        do
        {
            changed = false;
            for (Character c : getAlphabet())
            {
                for (UnorderedPair<State, State> pair : statePairs)
                {
                    final State p = pair.getLeft().transition(this, c);
                    final State q = pair.getRight().transition(this, c);
                    if (p == ERROR || q == ERROR)
                    {
                        continue;
                    }
                    if (separableStates.contains(new UnorderedPair<State, State>(p, q)) && !separableStates.contains(pair))
                    {
                        separableStates.add(pair);
                        changed = true;
                    }
                }
            }
        }
        while (changed);

        statePairs.removeAll(separableStates);

        for (UnorderedPair<State, State> pair : statePairs)
        {
            final State p = pair.getLeft();
            final State q = pair.getRight();

            states.remove(q);
            accepting.remove(q);
            if (start == q)
            {
                start = p;
            }

            for (ExpectedTransition t : transitions)
            {
                State origin = t.getOrigin();
                State destination = t.getDestination();
                if (origin.equals(q))
                {
                    origin = p;
                }
                if (destination.equals(q))
                {
                    destination = p;
                }

                if (origin != t.getOrigin() || destination != t.getDestination())
                {
                    transitions.remove(t);
                    transitions.add(new ExpectedTransition(origin, t.getWith(), destination));
                }
            }
        }


        return new DFA(states, new HashSet<ExpectedTransition>(transitions), start, accepting);
    }

    @Override
    public DFA complement()
    {
        return new DFA(getStates(), getTransitions(), getStartState(), complementaryAcceptingStates());
    }
}
