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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.RuleElement;
import de.cubeisland.engine.parser.rule.token.EndOfFileToken;
import de.cubeisland.engine.parser.rule.token.TokenClass;
import de.cubeisland.engine.parser.util.TokenString;

import static de.cubeisland.engine.parser.Util.asSet;
import static de.cubeisland.engine.parser.util.TokenString.concatMany;
import static de.cubeisland.engine.parser.util.TokenString.str;
import static java.util.Collections.emptySet;

public class GrammarUtils
{
    public static final Set<TokenString> EMPTY_TOKEN_STRING_SET = Collections.unmodifiableSet(asSet(TokenString.EMPTY));

    public static Set<Variable> nullClosure(final Set<Variable> variables, final Iterable<Rule> rules)
    {
        Set<Variable> nullable = new HashSet<Variable>();
        int oldSize;
        int newSize = 0;

        do
        {
            Set<Variable> notNullVariables = new HashSet<Variable>(variables);
            notNullVariables.removeAll(nullable);

            for (Variable variable : notNullVariables)
            {
                Set<Rule> rulesForVar = getRulesFor(variable, rules);
                for (Rule rule : rulesForVar)
                {
                    boolean allNullable = true;
                    for (RuleElement ruleElement : rule.getBody())
                    {
                        if (ruleElement instanceof TokenClass)
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

    public static Set<Rule> getRulesFor(Variable head, final Iterable<Rule> rules)
    {
        Set<Rule> matchedRules = new HashSet<Rule>();
        for (final Rule rule : rules)
        {
            if (rule.getHead() == head)
            {
                matchedRules.add(rule);
            }
        }
        return matchedRules;
    }

    private static Map<Variable, Set<TokenString>> initializedMap(final Set<Variable> variables)
    {
        final Map<Variable, Set<TokenString>> initializedMap = new HashMap<Variable, Set<TokenString>>();

        for (Variable variable : variables)
        {
            initializedMap.put(variable, new HashSet<TokenString>());
        }

        return initializedMap;
    }

    public static Map<Variable, Set<TokenString>> first(final Set<Variable> variables, final Iterable<Rule> rules)
    {
        return first(1, variables, rules);
    }

    public static Map<Variable, Set<TokenString>> first(int k, final Set<Variable> variables, final Iterable<Rule> rules)
    {
        final Map<Variable, Set<TokenString>> first = initializedMap(variables);
        final Set<TokenString> initialEmptySet = emptySet();

        boolean changed;
        do
        {
            changed = false;
            for (Rule rule : rules)
            {
                Set<TokenString> ruleFirst = initialEmptySet;

                for (RuleElement ruleElement : rule.getBody())
                {
                    if (ruleElement instanceof Variable)
                    {
                        ruleFirst = TokenString.concatMany(k, ruleFirst, first.get(ruleElement));
                    }
                    else if (ruleElement instanceof TokenClass)
                    {
                        ruleFirst = TokenString.concatMany(k, ruleFirst, asSet(str((TokenClass)ruleElement)));
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

    public static Map<Variable, Set<TokenString>> follow(final Variable start, final Set<Variable> variables, final Iterable<Rule> rules)
    {
        return follow(1, start, variables, rules);
    }

    public static Map<Variable, Set<TokenString>> follow(int k, final Variable start, final Set<Variable> variables, final Iterable<Rule> rules)
    {
        final Map<Variable, Set<TokenString>> follow = initializedMap(variables);
        final Map<Variable, Set<TokenString>> first = first(k, variables, rules);

        follow.put(start, asSet(str(EndOfFileToken.EOF)));

        boolean change;
        do
        {
            change = false;

            for (Rule rule : rules)
            {
                List<RuleElement> body = rule.getBody();

                for (int i = 0; i < body.size(); ++i)
                {
                    RuleElement currentElement = body.get(i);

                    if (currentElement instanceof Variable)
                    {
                        final Set<TokenString> currentFollow = follow.get(currentElement);
                        int oldSize = currentFollow.size();

                        Set<TokenString> tail = firstList(k, body.subList(i + 1, body.size()), first);

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

    private static Set<TokenString> firstList(int k, List<RuleElement> followingElements, Map<Variable, Set<TokenString>> firstSets)
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

            return TokenString.concatMany(k, asSet(str((TokenClass) firstElement)), firstR);
        }
    }

    public static Set<Variable> allVariablesOf(Iterable<Rule> rules)
    {
        Set<Variable> variables = new HashSet<Variable>();
        for (final Rule rule : rules)
        {
            variables.add(rule.getHead());
        }
        return variables;
    }

    public static Set<TokenClass> allTokensOf(Iterable<Rule> rules)
    {
        HashSet<TokenClass> tokens = new HashSet<TokenClass>();
        for (final Rule rule : rules)
        {
            for (final RuleElement element : rule.getBody())
            {
                if (element instanceof TokenClass)
                {
                    tokens.add((TokenClass)element);
                }
            }
        }
        return tokens;
    }
}
