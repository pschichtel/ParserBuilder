package de.cubeisland.engine.parser.type;

import de.cubeisland.engine.parser.AugmentedGrammar;
import de.cubeisland.engine.parser.CompiledGrammar;

public interface GrammarType
{
    CompiledGrammar compile(AugmentedGrammar grammar);
}
