package de.cubeisland.engine.parser.type;

import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.type.result.CompilationResult;

public interface GrammarType
{
    CompilationResult compile(AugmentedGrammar grammar);
}
