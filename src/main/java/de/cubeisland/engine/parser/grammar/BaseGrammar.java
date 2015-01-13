package de.cubeisland.engine.parser.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.RuleElement;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import de.cubeisland.engine.parser.util.TokenConcatter;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

public abstract class BaseGrammar
{
    private final Set<Variable> variables;
    private final Set<TokenSpec> tokens;
    private final Set<Variable> nullables;
    private final List<Rule> rules;
    private final Variable start;

    public BaseGrammar(Set<Variable> variables, Set<TokenSpec> tokens, List<Rule> rules, Variable start)
    {
        this.variables = unmodifiableSet(variables);
        this.tokens = unmodifiableSet(tokens);
        this.rules = unmodifiableList(rules);
        this.start = start;

        this.nullables = nullClosure();

    }

    protected Set<Variable> nullClosure()
    {
        Set<Variable> nullable = new HashSet<Variable>();
        int oldSize = 0;
        int newSize = 0;

        do
        {
            Set<Variable> notNullVariables = new HashSet<Variable>(variables);
            notNullVariables.removeAll(nullable);

            for (Variable variable : notNullVariables)
            {
                Set<Rule> rulesForVar = getRulesFor(variable);
                for (Rule rule : rulesForVar)
                {
                    boolean allNullable = true;
                    for (RuleElement ruleElement : rule.getBody())
                    {
                        if (ruleElement instanceof TokenSpec)
                        {
                            allNullable = false;
                            break;
                        }
                        else if (ruleElement instanceof Variable && !nullable.contains(ruleElement))
                        {
                            allNullable = false;
                            break;
                        }
                    }

                    if (allNullable)
                    {
                        nullable.add(variable);
                        break;
                    }
                }
            }

            oldSize = newSize;
            newSize = nullable.size();
        }
        while (oldSize < newSize);

        return nullable;
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

    public Map<Variable, Set<List<TokenSpec>>> first()
    {
        return first(1);
    }

    public Map<Variable, Set<List<TokenSpec>>> first(int k)
    {
        final Map<Variable, Set<List<TokenSpec>>> first = new HashMap<Variable, Set<List<TokenSpec>>>();

        for (Variable variable : this.variables)
        {
            first.put(variable, new HashSet<List<TokenSpec>>());
        }


        int oldHash;
        int newHash = first.hashCode();

        do
        {

            for (Rule rule : this.rules)
            {
                Set<List<TokenSpec>> ruleFirst = new HashSet<List<TokenSpec>>();
                ruleFirst.add(new ArrayList<TokenSpec>());

                for (RuleElement ruleElement : rule.getBody())
                {
                    if (ruleElement instanceof Variable)
                    {
                        ruleFirst = TokenConcatter.concatPrefix(k, ruleFirst, first.get(ruleElement));
                    }
                    else if (ruleElement instanceof TokenSpec)
                    {
                        List<TokenSpec> tokenFirstList = new ArrayList<TokenSpec>();
                        tokenFirstList.add((TokenSpec) ruleElement);

                        Set<List<TokenSpec>> firstOfToken = new HashSet<List<TokenSpec>>();
                        firstOfToken.add(tokenFirstList);

                        ruleFirst = TokenConcatter.concatPrefix(k, ruleFirst, firstOfToken);
                    }
                }

                first.get(rule.getHead()).addAll(ruleFirst);
            }

            oldHash = newHash;
            newHash = first.hashCode();
        }
        // TODO don't use hash codes
        while (oldHash != newHash);

        return first;
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
        return this.nullables.contains(variable);
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
