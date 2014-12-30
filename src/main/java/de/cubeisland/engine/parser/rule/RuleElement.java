package de.cubeisland.engine.parser.rule;

import de.cubeisland.engine.parser.Identified;

public class RuleElement extends Identified
{
    private final String name;

    public RuleElement(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
