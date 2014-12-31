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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }

        final ParseState that = (ParseState)o;

        if (!rules.equals(that.rules))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
