package de.cubeisland.engine.parser.rule.token;

public final class EndOfFileToken extends TokenSpec implements Token
{
    public static final EndOfFileToken EOF = new EndOfFileToken();

    public EndOfFileToken()
    {
        super("<EOF>");
    }

    @Override
    public String toString()
    {
        return "$";
    }
}
