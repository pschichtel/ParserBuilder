package de.cubeisland.engine.parser.util;

import java.util.HashSet;
import java.util.Set;

public class FixPoint
{
    public static <T> Set<T> apply(Set<T> in, Function<Set<T>, Set<T>> func)
    {
        Set<T> result = new HashSet<T>(in);
        Set<T> newSet = func.apply(in);

        newSet.removeAll(in);
        if (!newSet.isEmpty())
        {
            result.addAll(apply(newSet, func));
        }

        return result;
    }
}
