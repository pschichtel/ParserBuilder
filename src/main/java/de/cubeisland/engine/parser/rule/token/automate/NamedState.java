package de.cubeisland.engine.parser.rule.token.automate;

public class NamedState extends State
{
    private final String name;

    public NamedState(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
