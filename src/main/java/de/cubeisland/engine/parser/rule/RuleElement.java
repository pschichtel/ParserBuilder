package de.cubeisland.engine.parser.rule;

public class RuleElement
{
    private static volatile short idCounter = 0;

    private final short id;
    private final String name;

    public RuleElement(String name)
    {
        this.id = idCounter++;
        this.name = name;
    }

    public short getId()
    {
        return this.id;
    }

    public String getName()
    {
        return name;
    }
}
