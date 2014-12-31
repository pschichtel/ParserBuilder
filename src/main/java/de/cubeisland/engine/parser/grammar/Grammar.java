package de.cubeisland.engine.parser.grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.RuleElement;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

public class Grammar extends BaseGrammar
{
    public Grammar(Set<Variable> variables, Set<TokenSpec> tokens, List<Rule> rules, Variable start)
    {
        super(variables, tokens, rules, start);
    }

    public AugmentedGrammar augment()
    {
        return new AugmentedGrammar(getVariables(), getTokens(), getRules(), getStart());
    }

    public static Builder with(Rule rule)
    {
        return new Builder().with(rule);
    }

    public static final class Builder
    {
        private final List<Rule> rules = new ArrayList<Rule>();

        public Builder with(Rule rule)
        {
            this.rules.add(rule);
            return this;
        }

        public Grammar startingWith(Variable start) {
            return new Grammar(variables(rules), tokens(rules), rules, start);
        }

        private static Set<Variable> variables(List<Rule> rules)
        {
            Set<Variable> variables = new HashSet<Variable>();
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
                for (final RuleElement element : rule.getBody())
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
