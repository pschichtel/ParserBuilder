package de.cubeisland.engine.parser.type;

import java.util.Collections;
import java.util.Set;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.token.ParameterizedToken;

public class SLRType extends LRType
{
    @Override
    protected Set<ParameterizedToken> calculateFollows(Rule rule)
    {
        return Collections.emptySet();
    }
}
