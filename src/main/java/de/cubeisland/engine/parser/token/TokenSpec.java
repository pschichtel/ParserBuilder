package de.cubeisland.engine.parser.token;

import java.util.regex.Pattern;
import de.cubeisland.engine.parser.rule.RuleElement;

import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;

public class TokenSpec extends RuleElement
{
    private final Pattern pattern;
    private final String output;

    public TokenSpec(String name, String pattern)
    {
        this(name, compile(quote(pattern)), pattern);
    }

    public TokenSpec(String name, Pattern pattern)
    {
        this(name, pattern, "Regex(" + pattern.toString() + ")");
    }

    private TokenSpec(String name, Pattern pattern, String output)
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
        return getName() + " => " + this.output;
    }
}
