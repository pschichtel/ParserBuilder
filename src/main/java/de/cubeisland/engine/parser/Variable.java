package de.cubeisland.engine.parser;

public class Variable
{
    private final String name;

    public Variable(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return "Variable(" + this.name + ')';
    }
}
