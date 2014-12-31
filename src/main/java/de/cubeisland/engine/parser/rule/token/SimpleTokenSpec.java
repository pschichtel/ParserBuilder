package de.cubeisland.engine.parser.rule.token;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;

public class SimpleTokenSpec extends TokenSpec
{
    private final Pattern pattern;
    private final String output;

    public SimpleTokenSpec(String name, String pattern)
    {
        this(name, compile(quote(pattern)), pattern);
    }

    public SimpleTokenSpec(String name, Pattern pattern)
    {
        this(name, pattern, "Regex(" + pattern.toString() + ")");
    }

    private SimpleTokenSpec(String name, Pattern pattern, String output)
    {
        super(name);
        this.pattern = pattern;
        this.output = output;
    }

    public Pattern getPattern()
    {
        return pattern;
    }

    @Override
    public String toString()
    {
        return getName() + " ⇒ " + this.output;
    }
}
