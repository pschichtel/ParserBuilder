package de.cubeisland.engine.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.RuleElement;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

public class Grammar
{
    private final Set<Variable> variables;
    private final Set<TokenSpec> tokens;
    private final List<Rule> rules;
    private final Variable start;

    public Grammar(Set<Variable> variables, Set<TokenSpec> tokens, List<Rule> rules, Variable start)
    {
        this.variables = unmodifiableSet(variables);
        this.tokens = unmodifiableSet(tokens);
        this.rules = unmodifiableList(rules);
        this.start = start;
    }

    public AugmentedGrammar augment()
    {
        return new AugmentedGrammar(variables, tokens, rules, start);
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

    public Variable getStart()
    {
        return start;
    }

    public static Builder with(Variable head, RuleElement... elements)
    {
        return new Builder().and(head, elements);
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

    public static final class Builder
    {
        private final List<Rule> rules = new ArrayList<Rule>();

        public Builder and(Variable head, RuleElement... elements)
        {
            this.rules.add(new Rule(head, elements));
            return this;
        }

        public Grammar startingWith(Variable start) {
            return new Grammar(variables(rules), tokens(rules), rules, start);
        }

        private static Set<Variable> variables(List<Rule> rules)
        {
            HashSet<Variable> variables = new HashSet<Variable>();
            for (final Rule rule : rules)
            {
                variables.add(rule.getHead());
            }
            return variables;
        }

        private static Set<TokenSpec> tokens(List<Rule> rules)
        {
            HashSet<TokenSpec> tokens = new HashSet<TokenSpec>();
            for (final Rule rule : rules)
            {
                for (final RuleElement element : rule.getElements())
                {
                    if (element instanceof TokenSpec)
                    {
                        tokens.add((TokenSpec)element);
                    }
                }
            }
            return tokens;
        }
    }
}
