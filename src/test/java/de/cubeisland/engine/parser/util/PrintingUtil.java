package de.cubeisland.engine.parser.util;

import de.cubeisland.engine.parser.Variable;

import java.util.Map;
import java.util.Set;

public abstract class PrintingUtil
{
    private PrintingUtil()
    {}

    public static void printTokenStringMap(Map<Variable, Set<TokenString>> map)
    {
        for (Map.Entry<Variable, Set<TokenString>> entry : map.entrySet())
        {
            System.out.println(entry.getKey());
            for (TokenString tokenString : entry.getValue())
            {
                System.out.println("\t" + tokenString);
            }
        }
    }
}
