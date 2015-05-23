/**
 * The MIT License
 * Copyright (c) 2014 Cube Island
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.cubeisland.engine.parser.grammar;

import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.RuleElement;
import de.cubeisland.engine.parser.rule.token.EndOfFileToken;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import de.cubeisland.engine.parser.util.TokenString;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static de.cubeisland.engine.parser.Util.asSet;
import static de.cubeisland.engine.parser.util.TokenString.concatMany;
import static de.cubeisland.engine.parser.util.TokenString.str;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

public abstract class BaseGrammar
{
    public static final Set<TokenString> EMPTY_TOKEN_STRING_SET = Collections.unmodifiableSet(asSet(TokenString.EMPTY));
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

    public Map<Variable, Set<TokenString>> first()
    {
        return first(1);
    }

    public Map<Variable, Set<TokenString>> first(int k)
    {
        final Map<Variable, Set<TokenString>> first = this.initializedMap();
        final Set<TokenString> initialEmptySet = emptySet();

        boolean changed;
        do
        {
            changed = false;
            for (Rule rule : this.rules)
            {
                Set<TokenString> ruleFirst = initialEmptySet;

                for (RuleElement ruleElement : rule.getBody())
                {
                    if (ruleElement instanceof Variable)
                    {
                        ruleFirst = TokenString.concatMany(k, ruleFirst, first.get(ruleElement));
                    }
                    else if (ruleElement instanceof TokenSpec)
                    {
                        final TokenSpec token = (TokenSpec)ruleElement;
                        ruleFirst = TokenString.concatMany(k, ruleFirst, asSet(str(token)));
                    }
                }

                final Set<TokenString> firstSet = first.get(rule.getHead());
                final int oldSize = firstSet.size();
                firstSet.addAll(ruleFirst);

                if (oldSize != firstSet.size())
                {
                    changed = true;
                }
            }
        }
        while (changed);

        return first;
    }

    public Map<Variable, Set<TokenString>> initializedMap()
    {
        final Map<Variable, Set<TokenString>> initializedMap = new HashMap<Variable, Set<TokenString>>();

        for (Variable variable : this.getVariables())
        {
            initializedMap.put(variable, new HashSet<TokenString>());
        }

        return initializedMap;
    }


    public Map<Variable, Set<TokenString>> follow()
    {
        return follow(1);
    }

    public Map<Variable, Set<TokenString>> follow(int k)
    {
        final Map<Variable, Set<TokenString>> follow = this.initializedMap();
        final Map<Variable, Set<TokenString>> first = first(k);

        follow.put(this.getStart(), asSet(str(EndOfFileToken.EOF)));

        boolean change;
        do
        {
            change = false;

            for (Rule rule : this.getRules())
            {
                List<RuleElement> body = rule.getBody();

                for (int i = 0; i < body.size(); i++)
                {
                    RuleElement currentElement = body.get(i);

                    if (currentElement instanceof Variable)
                    {

                        final Set<TokenString> currentFollow = follow.get(currentElement);
                        int oldSize = currentFollow.size();

                        Set<TokenString> tail = firstList(k, body.subList(i+1, body.size()), first);

                        Set<TokenString> newFollow = concatMany(k, tail, follow.get(rule.getHead()));

                        currentFollow.addAll(newFollow);

                        if (currentFollow.size() > oldSize)
                        {
                            change = true;
                        }
                    }
                }
            }
        } while(change);


        return follow;
    }

    public Set<TokenString> firstList(int k, List<RuleElement> followingElements, Map<Variable, Set<TokenString>> firstSets)
    {
        if (followingElements.isEmpty())
        {
            return EMPTY_TOKEN_STRING_SET;
        }

        RuleElement firstElement = followingElements.get(0);

        if (firstElement instanceof Variable)
        {
            Set<TokenString> firstV = firstSets.get(firstElement);
            Set<TokenString> firstR = firstList(k, followingElements.subList(1, followingElements.size()), firstSets);

            return TokenString.concatMany(k, firstV, firstR);
        }
        else
        {
            Set<TokenString> firstR = firstList(k, followingElements.subList(1, followingElements.size()), firstSets);

            return TokenString.concatMany(k, asSet(str((TokenSpec) firstElement)), firstR);
        }
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
