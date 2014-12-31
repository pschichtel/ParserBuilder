package de.cubeisland.engine.parser.rule.token;

import java.util.regex.Pattern;

public class TokenSpecFactory
{
    private TokenSpecFactory()
    {
    }

    public static TokenSpec simple(String name, String pattern)
    {
        return new SimpleTokenSpec(name, pattern);
    }

    public static TokenSpec simple(String name, Pattern pattern)
    {
        return new SimpleTokenSpec(name, pattern);
    }

    public static <T> ParametrizedTokenSpec<T> parametrized(String name, String pattern, Class<T> valueType)
    {
        return new ParametrizedTokenSpec<T>(name, Pattern.compile(pattern), valueType);
    }
}
