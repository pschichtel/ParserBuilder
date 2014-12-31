package de.cubeisland.engine.parser.type.result;

import java.util.List;
import de.cubeisland.engine.parser.grammar.CompiledGrammar;

public abstract class CompilationResult
{
    protected CompilationResult()
    {
    }

    public static CompilationResult success(CompiledGrammar grammar)
    {
        return new SuccessfulResult(grammar);
    }

    public static CompilationResult failure(List<Conflict> conflicts)
    {
        return new FailedResult(conflicts);
    }
}
