package de.cubeisland.engine.parser;

public abstract class Identified
{
    private static volatile short idCounter = 0;

    private final short id = idCounter++;

    public short getId()
    {
        return this.id;
    }

    @Override
    public int hashCode()
    {
        return getId();
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
}
