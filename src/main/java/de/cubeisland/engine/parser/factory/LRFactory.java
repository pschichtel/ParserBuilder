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

import static de.cubeisland.engine.parser.Util.asSet;
import static de.cubeisland.engine.parser.factory.result.CompilationResult.success;

public class LRFactory implements ParserFactory<LRParser>
{
    public CompilationResult<LRParser> produce(AugmentedGrammar g)
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

    protected Set<MarkedRule> closure(AugmentedGrammar g, Set<MarkedRule> rules)
    {
        Set<MarkedRule> closure = new HashSet<MarkedRule>(rules);
        Set<MarkedRule> newRules = new HashSet<MarkedRule>();
        for (final MarkedRule rule : rules)
        {
            RuleElement marked = rule.getMarkedElement();
            if (marked instanceof Variable)
            {
                newRules.addAll(markAll(g.getRulesFor((Variable)marked)));
            }
        }

        newRules.removeAll(rules);
        if (!newRules.isEmpty())
        {
            closure.addAll(closure(g, newRules));
        }

        return closure;
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
