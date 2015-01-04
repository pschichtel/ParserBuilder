package de.cubeisland.engine.parser.factory;

import de.cubeisland.engine.parser.factory.result.CompilationResult;
import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.parser.Parser;

public interface ParserFactory<T extends Parser>
{
    CompilationResult<T> produce(AugmentedGrammar grammar);
}
