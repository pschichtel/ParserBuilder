package de.cubeisland.engine.parser.rule.token.automate.action;

import de.cubeisland.engine.parser.rule.token.Token;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

public interface TokenAction
{
    Token act(TokenSpec spec, String s);
}
