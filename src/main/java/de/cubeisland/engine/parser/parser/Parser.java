package de.cubeisland.engine.parser.parser;

import de.cubeisland.engine.parser.rule.token.TokenStream;

public interface Parser
{
    boolean parse(TokenStream tokenStream);
}
