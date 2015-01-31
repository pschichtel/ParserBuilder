package de.cubeisland.engine.parser.rule.token;

import java.io.IOException;

public interface InputSource
{
    boolean isDepleted();
    char read() throws IOException;
}
