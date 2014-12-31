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
        StringBuilder out = new StringBuilder("s").append(getId()).append("(\n");
        for (final MarkedRule rule : rules)
        {
            out.append('\t').append(rule).append('\n');
        }
        out.append(')');
        return out.toString();
    }
}
