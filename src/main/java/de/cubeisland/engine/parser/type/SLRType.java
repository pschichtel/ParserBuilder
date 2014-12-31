package de.cubeisland.engine.parser.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

public class SLRType extends LRType
{
    private final Map<Rule, Set<TokenSpec>> followSets = new HashMap<Rule, Set<TokenSpec>>();

    @Override
    protected Set<TokenSpec> calculateFollows(AugmentedGrammar g, Rule rule)
    {
        Set<TokenSpec> followSet = this.followSets.get(rule);
        if (followSet == null)
        {
            followSet = g.follow(rule);
            this.followSets.put(rule, followSet);
        }
        return followSet;
    }
}
