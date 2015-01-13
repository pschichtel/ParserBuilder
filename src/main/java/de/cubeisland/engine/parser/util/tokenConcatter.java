package de.cubeisland.engine.parser.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.cubeisland.engine.parser.rule.token.Epsilon;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

public class tokenConcatter
{
    public static List<TokenSpec> concatPrefix(int k, List<TokenSpec> v, List<TokenSpec> w)
    {
        List<TokenSpec> concatedList = new ArrayList<TokenSpec>(v);
        concatedList.addAll(w);
        return concatedList.subList(0, k < concatedList.size() ? k : concatedList.size());
    }

    public static Set<List<TokenSpec>> concatPrefix(int k, Set<List<TokenSpec>> m, Set<List<TokenSpec>> n)
    {
        Set<List<TokenSpec>> result = new HashSet<List<TokenSpec>>();
        for (List<TokenSpec> v : m)
        {
            for (List<TokenSpec> w : n)
            {
                result.add(concatPrefix(k, v, w));
            }
        }
        return result;
    }

    public static Set<List<TokenSpec>> concatPrefix(int k, Set<List<TokenSpec>>... tokenLists)
    {
        Set<List<TokenSpec>> result = new HashSet<List<TokenSpec>>();
        result.add(new ArrayList<TokenSpec>());

        for (Set<List<TokenSpec>> m : tokenLists)
        {
            result = concatPrefix(k, result, m);
        }

        return result;
    }
}
