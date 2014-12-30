package de.cubeisland.engine.parser.rule.token;


public class SimpleToken implements Token
{
    private final TokenSpec spec;

    public SimpleToken(TokenSpec spec)
    {
        this.spec = spec;
    }
}
