package de.cubeisland.engine.parser.rule.token.automate;

import static de.cubeisland.engine.parser.rule.token.Epsilon.EPSILON;

public class SpontaneousTransition implements Transition
{
    private final State origin;
    private final State destination;

    public SpontaneousTransition(State origin, State destination)
    {
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public State getOrigin()
    {
        return this.origin;
    }

    @Override
    public State getDestination()
    {
        return this.destination;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof SpontaneousTransition))
        {
            return false;
        }

        SpontaneousTransition that = (SpontaneousTransition) o;

        if (!destination.equals(that.destination))
        {
            return false;
        }
        if (!origin.equals(that.origin))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = origin.hashCode();
        result = 31 * result + destination.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return getOrigin() + " ---" + EPSILON + "---> " + getDestination();
    }
}
