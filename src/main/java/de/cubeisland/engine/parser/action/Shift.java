package de.cubeisland.engine.parser.action;

import de.cubeisland.engine.parser.ParseState;

public class Shift extends Action
{
    private final ParseState state;

    public Shift(ParseState state)
    {
        this.state = state;
    }

    public ParseState getState()
    {
        return state;
    }

    @Override
    public String toString()
    {
        return super.toString() + "(" + state + ")";
    }
}
