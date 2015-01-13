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

        public Grammar startingWith(Variable start)
        {
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
