package de.cubeisland.engine.parser;

import java.util.ArrayList;
import java.util.List;

public class State
{
    private final List<MarkedRule> rules;

    public State(List<MarkedRule> rules)
    {
        this.rules = new ArrayList<MarkedRule>(rules);
    }

    public List<MarkedRule> getRules()
    {
        return rules;
    }
}
