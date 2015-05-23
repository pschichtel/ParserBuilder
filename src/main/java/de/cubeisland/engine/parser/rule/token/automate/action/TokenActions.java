package de.cubeisland.engine.parser.rule.token.automate.action;

public abstract class TokenActions
{
    private TokenActions()
    {}

    public static final TokenAction STRING = new StringAction();
    public static final TokenAction INTEGER = new IntegerAction();
}
