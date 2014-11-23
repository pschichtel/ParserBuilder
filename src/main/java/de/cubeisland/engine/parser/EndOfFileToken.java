package de.cubeisland.engine.parser;

public final class EndOfFileToken extends Token
{
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
