package de.cubeisland.engine.parser.rule.token.automate;

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

    public DFA minimize()
    {
        final Set<State> states = getReachableStates();
        final Set<ExpectedTransition> transitions = new CopyOnWriteArraySet<ExpectedTransition>(getTransitions());
        State start = getStartState();
        final Set<State> accepting = new HashSet<State>(getAcceptingStates());


        Set<StatePair> statePairs = new HashSet<StatePair>();

        for (State p : states)
        {
            for (State q : states)
            {
                if (p != q)
                {
                    statePairs.add(new StatePair(p, q));
                }
            }
        }

        Set<StatePair> separableStates = new HashSet<StatePair>();
        for (StatePair p : statePairs)
        {
            if (isAccepting(p.left) && !isAccepting(p.right) || !isAccepting(p.left) && isAccepting(p.right))
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
                for (StatePair pair : statePairs)
                {
                    State p = pair.left.transition(this, c);
                    State q = pair.right.transition(this, c);
                    if (p == ERROR || q == ERROR)
                    {
                        continue;
                    }
                    if (separableStates.contains(new StatePair(p, q)) && !separableStates.contains(pair))
                    {
                        separableStates.add(pair);
                        changed = true;
                    }
                }
            }
        }
        while (changed);

        statePairs.removeAll(separableStates);

        for (StatePair pair : statePairs)
        {
            states.remove(pair.right);
            accepting.remove(pair.right);
            if (start == pair.right)
            {
                start = pair.left;
            }

            for (ExpectedTransition t : transitions)
            {
                State origin = t.getOrigin();
                State destination = t.getDestination();
                if (origin.equals(pair.right))
                {
                    origin = pair.left;
                }
                if (destination.equals(pair.right))
                {
                    destination = pair.left;
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

    private class StatePair
    {
        public final State left;
        public final State right;

        public StatePair(State left, State right)
        {
            this.left = left;
            this.right = right;
        }

        /**
         * This implementation ignores the order of the fields, so <a, b> = <b, a>
         *
         * @param o the other object
         * @return true of they're the same
         */
        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (!(o instanceof StatePair))
            {
                return false;
            }

            StatePair that = (StatePair) o;

            if (left.equals(that.left) && right.equals(that.right))
            {
                return true;
            }
            if (left.equals(that.right) && right.equals(that.left))
            {
                return true;
            }
            return false;
        }

        /**
         * This implementation just adds up both hashCodes to drop ordering
         *
         * @return hash code
         */
        @Override
        public int hashCode()
        {
            return left.hashCode() + right.hashCode();
        }

        @Override
        public String toString()
        {
            return "<" + this.left + ", " + this.right + ">";
        }
    }
}
