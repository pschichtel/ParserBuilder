package de.cubeisland.engine.parser.grammar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.RuleElement;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

public abstract class BaseGrammar
{
    private final Set<Variable> variables;
    private final Set<TokenSpec> tokens;
    private final List<Rule> rules;
    private final Variable start;

    public BaseGrammar(Set<Variable> variables, Set<TokenSpec> tokens, List<Rule> rules, Variable start)
    {
        this.variables = unmodifiableSet(variables);
        this.tokens = unmodifiableSet(tokens);
        this.rules = unmodifiableList(rules);
        this.start = start;
    }

    public Set<Variable> getVariables()
    {
        return variables;
    }

    public Set<TokenSpec> getTokens()
    {
        return tokens;
    }

    public List<Rule> getRules()
    {
        return rules;
    }

    public Set<Rule> getRulesFor(Variable head)
    {
        Set<Rule> rules = new HashSet<Rule>();
        for (final Rule rule : this.rules)
        {
            if (rule.getHead() == head)
            {
                rules.add(rule);
            }
        }
        return rules;
    }

    public Set<Rule> getRulesContaining(RuleElement element)
    {
        Set<Rule> rules = new HashSet<Rule>();

        for (final Rule rule : this.rules)
        {
            if (rule.getBody().contains(element))
            {
                rules.add(rule);
            }
        }

        return rules;
    }

    public boolean isNullable(Variable variable)
    {
        for (final Rule rule : getRulesFor(variable))
        {
            if (rule.isEpsilonProducing())
            {
                return true;
            }
        }
        return false;
    }

    public Set<TokenSpec> first(Rule rule)
    {
        return first(rule.getHead());
    }

    public Set<TokenSpec> first(Variable variable)
    {
        Set<TokenSpec> first = new HashSet<TokenSpec>();
        first(variable, first);
        return first;
    }

    private void first(Variable variable, Set<TokenSpec> firstSet)
    {
        first(variable, 0, firstSet);
    }

    private void first(Variable variable, int offset, Set<TokenSpec> firstSet)
    {
        for (final Rule rule : getRulesFor(variable))
        {
            RuleElement first = rule.getBody().get(offset);
            if (first instanceof TokenSpec)
            {
                firstSet.add((TokenSpec)first);
            }
            else if (first instanceof Variable && first != rule.getHead())
            {
                Variable var = (Variable)first;
                first(var, firstSet);
                if (isNullable(var))
                {
                    first(variable, offset + 1, firstSet);
                }
            }
        }
    }

    public Variable getStart()
    {
        return start;
    }

    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder(getClass().getSimpleName() + "(\n");

        for (final TokenSpec token : getTokens())
        {
            out.append('\t').append(token).append('\n');
        }

        for (final Rule rule : getRules())
        {
            out.append('\t').append(rule).append('\n');
        }
        return out.append(")").toString();
    }
}
