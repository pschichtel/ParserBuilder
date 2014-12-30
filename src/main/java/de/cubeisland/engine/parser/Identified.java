package de.cubeisland.engine.parser;

import java.util.Set;
import de.cubeisland.engine.parser.rule.Rule.MarkedRule;

public abstract class Identified
{
    private static volatile short idCounter = 0;

    private final short id = idCounter++;

    public short getId()
    {
        return this.id;
    }
}
