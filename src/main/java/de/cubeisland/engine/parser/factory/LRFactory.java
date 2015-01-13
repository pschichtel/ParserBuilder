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
package de.cubeisland.engine.parser.factory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import de.cubeisland.engine.parser.ActionTable;
import de.cubeisland.engine.parser.GotoTable;
import de.cubeisland.engine.parser.LRParser;
import de.cubeisland.engine.parser.ParseState;
import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.action.Action;
import de.cubeisland.engine.parser.factory.result.CompilationResult;
import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.Rule.MarkedRule;
import de.cubeisland.engine.parser.rule.RuleElement;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import de.cubeisland.engine.parser.util.FixPoint;
import de.cubeisland.engine.parser.util.SetMapper;

import static de.cubeisland.engine.parser.Util.asSet;
import static de.cubeisland.engine.parser.factory.result.CompilationResult.success;

public class LRFactory implements ParserFactory<LRParser>
{
    public CompilationResult<LRParser> produce(AugmentedGrammar g, int k)
    {
        final ParseState initial = calculateInitialState(g);
        final Set<RuleElement> elements = new HashSet<RuleElement>(g.getVariables());
        elements.addAll(g.getTokens());

        final Set<ParseState> states = asSet(initial);
        final Map<ActionTable.Entry, Action> actions = new HashMap<ActionTable.Entry, Action>();
        final Map<GotoTable.Entry, ParseState> gotos = new HashMap<GotoTable.Entry, ParseState>();


        ParseState state = initial;
//        while (true);
//        {
//
//            Set<ParseState> parseStates = new HashSet<ParseState>();
//            for (final RuleElement element : elements)
//            {
//                parseStates.add(goTo(g, state, element));
//            }
//        }

        return success(new LRParser(g, states, new GotoTable(gotos), new ActionTable(actions)));
    }

    protected ParseState calculateInitialState(AugmentedGrammar g)
    {
        return new ParseState(this.closure(g, asSet(g.getStartRule().mark())));
    }

    public ParseState goTo(AugmentedGrammar g, ParseState initial, RuleElement element)
    {
        Set<MarkedRule> consumed = new HashSet<MarkedRule>();

        for (final MarkedRule rule : initial.getRules())
        {
            if (rule.getMarkedElement() == element)
            {
                consumed.add(rule.consumeOne());
            }
        }

        if (consumed.equals(initial.getRules()))
        {
            return initial;
        }

        return new ParseState(closure(g, consumed));
    }

    protected Set<MarkedRule> closure(final AugmentedGrammar g, Set<MarkedRule> rules)
    {
        return FixPoint.apply(rules, new SetMapper<MarkedRule>()
        {
            public Set<MarkedRule> apply(Set<MarkedRule> in)
            {
                Set<MarkedRule> newRules = new HashSet<MarkedRule>();
                for (final MarkedRule rule : in)
                {
                    RuleElement marked = rule.getMarkedElement();
                    if (marked instanceof Variable)
                    {
                        newRules.addAll(markAll(g.getRulesFor((Variable)marked)));
                    }
                }
                return newRules;
            }
        });
    }

    protected Set<TokenSpec> calculateFollows(AugmentedGrammar g, Rule rule)
    {
        return Collections.emptySet();
    }

    protected static Set<MarkedRule> markAll(Set<Rule> rules)
    {
        Set<MarkedRule> marked = new HashSet<MarkedRule>();

        for (final Rule rule : rules)
        {
            marked.add(rule.mark());
        }

        return marked;
    }
}
