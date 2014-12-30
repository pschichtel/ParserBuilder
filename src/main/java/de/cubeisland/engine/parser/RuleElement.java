package de.cubeisland.engine.parser;

public class RuleElement
{
    private static volatile short idCounter = 0;

    private final short id;

    {
        this.id = idCounter++;
    }

    public short getId()
    {
        return this.id;
    }
}
