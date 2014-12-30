package de.cubeisland.engine.parser.type;

import java.util.Collections;
import java.util.Set;
import de.cubeisland.engine.parser.CompiledGrammar;
import de.cubeisland.engine.parser.Grammar;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.token.ParameterizedToken;

public class LRType implements GrammarType
{
    public CompiledGrammar compile(Grammar grammar)
    {
        return null;
    }

    protected Set<ParameterizedToken> calculateFollows(Rule r)
    {
        return Collections.emptySet();
    }
}
