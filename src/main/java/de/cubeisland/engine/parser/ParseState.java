package de.cubeisland.engine.parser;

import java.util.ArrayList;
import java.util.List;

public class ParseState
{
    private static volatile short idCounter = 0;

    private final short id;

    {
        this.id = idCounter++;
    }

    private final List<MarkedRule> rules;

    public ParseState(List<MarkedRule> rules)
    {
        this.rules = new ArrayList<MarkedRule>(rules);
    }

    public List<MarkedRule> getRules()
    {
        return rules;
    }

    public short getId()
    {
        return this.id;
    }
}
