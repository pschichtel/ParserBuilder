package de.cubeisland.engine.parser.action;

import de.cubeisland.engine.parser.ParseState;

public class Shift implements Action
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
}
