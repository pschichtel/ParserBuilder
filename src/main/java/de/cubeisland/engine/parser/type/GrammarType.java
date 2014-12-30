package de.cubeisland.engine.parser.type;

import de.cubeisland.engine.parser.CompiledGrammar;
import de.cubeisland.engine.parser.Grammar;

public interface GrammarType
{
    CompiledGrammar compile(Grammar grammar);
}
