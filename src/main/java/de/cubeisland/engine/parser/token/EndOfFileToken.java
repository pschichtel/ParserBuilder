package de.cubeisland.engine.parser.token;

public final class EndOfFileToken implements Token
{
    public static final EndOfFileToken EOF = new EndOfFileToken();

    @Override
    public String toString()
    {
        return "$";
    }
}
