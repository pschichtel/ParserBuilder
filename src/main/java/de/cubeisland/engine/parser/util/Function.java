package de.cubeisland.engine.parser.util;

public interface Function<I, O>
{
    O apply(I in);
}
