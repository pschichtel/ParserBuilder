package de.cubeisland.engine.parser;

import java.util.regex.Pattern;

public class TokenSpec
{
    private final Pattern pattern;

    public TokenSpec(Pattern pattern)
    {
        this.pattern = pattern;
    }

    public Pattern getPattern()
    {
        return pattern;
    }
}
