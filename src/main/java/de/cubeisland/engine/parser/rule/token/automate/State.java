package de.cubeisland.engine.parser.rule.token.automate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class State
{
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final int id = COUNTER.getAndIncrement();

    public State transition(DFA a, char c)
    {
        return a.getBy(this, c);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof State)) return false;

        State state = (State) o;

        return id == state.id;
    }

    @Override
    public int hashCode()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "State(" + this.id + ")";
    }
}
