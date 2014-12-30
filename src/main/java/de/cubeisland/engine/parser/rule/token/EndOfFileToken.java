package de.cubeisland.engine.parser.rule.token;

import de.cubeisland.engine.parser.rule.RuleElement;

public final class EndOfFileToken extends RuleElement implements Token
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
