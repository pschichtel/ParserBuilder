package de.cubeisland.engine.parser;

import java.util.Set;
import de.cubeisland.engine.parser.rule.Rule.MarkedRule;

import static java.util.Collections.unmodifiableSet;

public class ParseState extends Identified
{
    private final Set<MarkedRule> rules;

    public ParseState(Set<MarkedRule> rules)
    {
        this.rules = unmodifiableSet(rules);
    }

    public Set<MarkedRule> getRules()
    {
        return rules;
    }

    @Override
    public String toString()
    {
        return "s" + getId();
    }
}
