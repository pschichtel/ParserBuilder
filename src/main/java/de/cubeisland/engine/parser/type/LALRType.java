package de.cubeisland.engine.parser.type;

import de.cubeisland.engine.parser.CompiledGrammar;
import de.cubeisland.engine.parser.Grammar;

public class LALRType extends LRType
{
    public CompiledGrammar compile(Grammar grammar)
    {
        CompiledGrammar cg = super.compile(grammar);
        return optimize(cg);
    }

    protected CompiledGrammar optimize(CompiledGrammar cg)
    {
        return cg;
    }
}
