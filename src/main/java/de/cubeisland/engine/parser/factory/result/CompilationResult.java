package de.cubeisland.engine.parser.factory.result;

import java.util.List;
import de.cubeisland.engine.parser.parser.Parser;

public abstract class CompilationResult<T extends Parser>
{
    protected CompilationResult()
    {
    }

    public static <T extends Parser> CompilationResult<T> success(T parser)
    {
        return new SuccessfulResult<T>(parser);
    }

    public static <T extends Parser> CompilationResult<T> failure(List<Conflict> conflicts)
    {
        return new FailedResult<T>(conflicts);
    }
}
