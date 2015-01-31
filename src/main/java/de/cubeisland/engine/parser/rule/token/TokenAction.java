package de.cubeisland.engine.parser.rule.token;

public interface TokenAction
{
    Token act(TokenSpec spec, String s);
}
