package de.cubeisland.engine.parser.rule.token.automate;

public class ExpectedTransition implements Transition
{
    private final State origin;
    private final char with;
    private final State destination;

    public ExpectedTransition(State origin, char with, State destination)
    {
        this.origin = origin;
        this.with = with;
        this.destination = destination;
    }

    @Override
    public State getOrigin() {
        return this.origin;
    }

    @Override
    public State getDestination() {
        return this.destination;
    }

    public char getWith()
    {
        return this.with;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ExpectedTransition))
        {
            return false;
        }

        ExpectedTransition that = (ExpectedTransition) o;

        if (with != that.with)
        {
            return false;
        }
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
        result = 31 * result + (int) with;
        result = 31 * result + destination.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return getOrigin() + " --'" + this.with + "'-->  " + getDestination();
    }
}
