package de.cubeisland.engine.parser;

import de.cubeisland.engine.parser.rule.RuleElement;

public class Variable extends RuleElement
{
    public Variable(String name)
    {
        super(name);
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "(" + getName() + ')';
    }
}
