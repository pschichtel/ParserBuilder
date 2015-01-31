package de.cubeisland.engine.parser.rule.token;

import java.io.IOException;

public interface Tokenizer
{
    Token nextToken(InputSource source) throws IOException;
}
