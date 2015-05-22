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
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.cubeisland.engine.parser.rule.Reaction.SkipReaction.SKIP;
import static de.cubeisland.engine.parser.rule.token.EndOfFileToken.EOF;
import static java.util.Arrays.asList;

public class AugmentedGrammar extends BaseGrammar
{
    public static final Variable AUGMENTED_START = new AugmentedStartVariable();

    public AugmentedGrammar(Set<Variable> variables, Set<TokenSpec> tokens, List<Rule> rules, Variable start)
    {
        super(augment(variables), augmentTokens(tokens), augment(rules, start), AUGMENTED_START);
    }

    public Rule getStartRule()
    {
        return getRules().get(0);
    }



    private static Set<Variable> augment(Set<Variable> original)
    {
        Set<Variable> augmented = new HashSet<Variable>(original);
        augmented.add(AUGMENTED_START);
        return augmented;
    }

    private static Set<TokenSpec> augmentTokens(Set<TokenSpec> original)
    {
        Set<TokenSpec> augmented = new HashSet<TokenSpec>(original);
        augmented.add(EOF);
        return augmented;
    }

    private static List<Rule> augment(List<Rule> original, Variable originalStart)
    {
        List<Rule> rules = new ArrayList<Rule>(original);
        rules.add(0, new Rule(AUGMENTED_START, asList(originalStart, EOF), SKIP));
        return rules;
    }

    public static final class AugmentedStartVariable extends Variable
    {
        public AugmentedStartVariable()
        {
            super("<Start>");
        }
    }
}
