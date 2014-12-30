package de.cubeisland.engine.parser.action;

import de.cubeisland.engine.parser.rule.Rule;

public class Reduce implements Action
{
    private final Rule rule;

    public Reduce(Rule rule)
    {
        this.rule = rule;
    }

    public Rule getRule()
    {
        return rule;
    }
}
