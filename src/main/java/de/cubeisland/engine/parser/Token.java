package de.cubeisland.engine.parser;

public class Token extends RuleElement
{
    private final String token;

    public Token(String token)
    {
        this.token = token;
    }

    @Override
    public String toString()
    {
        return "Token(" + this.token + ')';
    }
}
