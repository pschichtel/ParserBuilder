package de.cubeisland.engine.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.RuleElement;
import de.cubeisland.engine.parser.token.TokenSpec;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

public class Grammar
{
    private final Set<Variable> variables;
    private final Set<TokenSpec> tokens;
    private final List<Rule> rules;
    private final Rule startRule;

    public Grammar(Set<Variable> variables, Set<TokenSpec> tokens, List<Rule> rules, Rule startRule)
    {
        this.variables = unmodifiableSet(variables);
        this.tokens = unmodifiableSet(tokens);
        this.rules = unmodifiableList(rules);
        this.startRule = startRule;
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

    public Rule getStartRule()
    {
        return startRule;
    }

    public static Builder build()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private final List<Rule> rules = new ArrayList<Rule>();

        public Builder with(Variable head, RuleElement... elements)
        {
            this.rules.add(new Rule(head, elements));
            return this;
        }

        public Builder and(Variable head, RuleElement... elements)
        {
            return this.with(head, elements);
        }

        public Grammar get() {
            return new Grammar(variables(rules), tokens(rules), rules, rules.get(0));
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
