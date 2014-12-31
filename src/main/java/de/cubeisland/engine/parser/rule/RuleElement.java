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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o instanceof RuleElement && getName().equals(((RuleElement)o).getName()))
        {
            return true;
        }

        return false;
    }
}
