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

import de.cubeisland.engine.parser.*;
import de.cubeisland.engine.parser.GotoTable.Entry;
import de.cubeisland.engine.parser.action.Action;
import de.cubeisland.engine.parser.action.Reduce;
import de.cubeisland.engine.parser.action.Shift;
import de.cubeisland.engine.parser.factory.result.CompilationResult;
import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.grammar.GrammarUtils;
import de.cubeisland.engine.parser.parser.LRParser;
import de.cubeisland.engine.parser.parser.ParseState;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.Rule.MarkedRule;
import de.cubeisland.engine.parser.rule.RuleElement;
import de.cubeisland.engine.parser.rule.token.TokenClass;
import de.cubeisland.engine.parser.rule.token.tokenizer.AutomateTokenizer;
import de.cubeisland.engine.parser.util.FixPoint;
import de.cubeisland.engine.parser.util.Function;
import de.cubeisland.engine.parser.util.TokenString;

import java.util.*;

import static de.cubeisland.engine.parser.Util.asSet;
import static de.cubeisland.engine.parser.action.Accept.ACCEPT;
import static de.cubeisland.engine.parser.factory.result.CompilationResult.success;
import static de.cubeisland.engine.parser.grammar.GrammarUtils.allVariablesOf;

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

        Map<ParseState, ParseState> knownStates = new HashMap<ParseState, ParseState>();
        Queue<ParseState> stateQueue = new LinkedList<ParseState>();
        stateQueue.offer(initial);
        while (!stateQueue.isEmpty())
        {
            ParseState current = stateQueue.poll();
            for (final RuleElement element : current.getReadableElements())
            {
                ParseState newState = goTo(g, current, element);
                ParseState targetState = knownStates.get(newState);
                if (targetState == null)
                {
                    targetState = newState;
                    stateQueue.offer(newState);
                    states.add(newState);
                    knownStates.put(newState, newState);
                }

                if (element instanceof TokenClass)
                {
                    final TokenClass token = (TokenClass)element;
                    actions.put(new ActionTable.Entry(current, token), new Shift(targetState)); // TODO this doesn't seem right...
                }
                else
                {
                    gotos.put(new Entry(current, element), targetState);
                }
            }

            for (final MarkedRule markedRule : current.getMarkedRules())
            {
                if (markedRule.isFinished())
                {
                    final Rule rule = markedRule.getRule();
                    for (final TokenString followString : getFollows(g, current, rule, k))
                    {
                        Action action;
                        if (rule.getHead() == g.getStart())
                        {
                            action = ACCEPT;
                        }
                        else
                        {
                            action = new Reduce(rule);
                        }
                        actions.put(new ActionTable.Entry(current, followString.tokenAt(0)), action);
                    }
                }
            }
        }

        return success(new LRParser(AutomateTokenizer.fromGrammar(g), states, new GotoTable(gotos), new ActionTable(actions)));
    }

    protected ParseState calculateInitialState(AugmentedGrammar g)
    {
        return new ParseState(closure(g, asSet(g.getStartRule().mark())));
    }

    public ParseState goTo(AugmentedGrammar g, ParseState current, RuleElement element)
    {
        Set<MarkedRule> consumed = new HashSet<MarkedRule>();

        for (final MarkedRule rule : current.getMarkedRules())
        {
            if (rule.getMarkedElement() == element)
            {
                consumed.add(rule.consumeOne());
            }
        }

        if (consumed.equals(current.getMarkedRules()))
        {
            return current;
        }

        return new ParseState(closure(g, consumed));
    }

    protected Set<MarkedRule> closure(final AugmentedGrammar g, Set<MarkedRule> rules)
    {
        return FixPoint.apply(rules, new Function<MarkedRule, Set<MarkedRule>>()
        {
            @Override
            public Set<MarkedRule> apply(MarkedRule in)
            {
                Set<MarkedRule> newRules = new HashSet<MarkedRule>();
                RuleElement marked = in.getMarkedElement();
                if (marked instanceof Variable)
                {
                    newRules.addAll(markAll(g.getRulesFor((Variable)marked)));
                }
                return newRules;
            }
        });
    }

    protected Set<TokenString> getFollows(AugmentedGrammar g, ParseState state, Rule rule, int k)
    {
        final Variable head = rule.getHead();
        final Set<Rule> rules = state.getRules();
        return GrammarUtils.follow(k, head, allVariablesOf(rules), rules).get(head);
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
