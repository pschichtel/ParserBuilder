package de.cubeisland.engine.parser.type;

import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.grammar.CompiledGrammar;
import de.cubeisland.engine.parser.type.result.CompilationResult;
import de.cubeisland.engine.parser.type.result.SuccessfulResult;

import static de.cubeisland.engine.parser.type.result.CompilationResult.success;

public class LALRType extends LRType
{
    public CompilationResult compile(AugmentedGrammar g)
    {
        CompilationResult result = super.compile(g);
        if (result instanceof SuccessfulResult)
        {
            return optimize(((SuccessfulResult)result).getGrammar());
        }
        return result;
    }

    protected CompilationResult optimize(CompiledGrammar cg)
    {
        return success(cg);
    }
}
