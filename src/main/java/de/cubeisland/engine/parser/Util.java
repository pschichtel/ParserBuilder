package de.cubeisland.engine.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Util
{
    private Util()
    {
    }


    public static <T> Set<T> asSet(T... elements)
    {
        return new HashSet<T>(Arrays.asList(elements));
    }
}
