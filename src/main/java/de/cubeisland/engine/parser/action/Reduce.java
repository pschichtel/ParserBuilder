package de.cubeisland.engine.parser.action;

import de.cubeisland.engine.parser.rule.Rule;

public class Reduce extends Action
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

    @Override
    public String toString()
    {
        return super.toString() + "(" + rule + ")";
    }
}
