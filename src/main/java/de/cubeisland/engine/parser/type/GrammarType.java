package de.cubeisland.engine.parser.type;

import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.grammar.CompiledGrammar;

public interface GrammarType
{
    CompiledGrammar compile(AugmentedGrammar grammar);
}
