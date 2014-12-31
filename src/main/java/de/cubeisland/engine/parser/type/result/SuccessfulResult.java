package de.cubeisland.engine.parser.type.result;

import de.cubeisland.engine.parser.grammar.CompiledGrammar;

public class SuccessfulResult extends CompilationResult
{
    private final CompiledGrammar grammar;

    public SuccessfulResult(CompiledGrammar grammar)
    {
        this.grammar = grammar;
    }

    public CompiledGrammar getGrammar()
    {
        return grammar;
    }
}
