package de.cubeisland.engine.parser.factory.result;

import de.cubeisland.engine.parser.parser.Parser;

public class SuccessfulResult<T extends Parser> extends CompilationResult<T>
{
    private final T parser;

    public SuccessfulResult(T parser)
    {
        this.parser = parser;
    }

    public T getParser()
    {
        return parser;
    }
}
