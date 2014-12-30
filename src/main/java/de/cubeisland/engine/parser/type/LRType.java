package de.cubeisland.engine.parser.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import de.cubeisland.engine.parser.ActionTable;
import de.cubeisland.engine.parser.AugmentedGrammar;
import de.cubeisland.engine.parser.CompiledGrammar;
import de.cubeisland.engine.parser.GotoTable;
import de.cubeisland.engine.parser.ParseState;
import de.cubeisland.engine.parser.action.Action;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.Rule.MarkedRule;
import de.cubeisland.engine.parser.rule.token.ParameterizedToken;

public class LRType implements GrammarType
{
    public CompiledGrammar compile(AugmentedGrammar g)
    {
        Set<MarkedRule> in = asSet(g.getStartRule().mark());
        Set<MarkedRule> out = calculateClosure(in);

        Set<ParseState> states = new HashSet<ParseState>();
        Map<ActionTable.Entry, Action> actions = new HashMap<ActionTable.Entry, Action>();
        Map<GotoTable.Entry, ParseState> gotos = new HashMap<GotoTable.Entry, ParseState>();

        return new CompiledGrammar(g, states, new GotoTable(gotos), new ActionTable(actions));
    }

    protected Set<ParameterizedToken> calculateFollows(Rule rule)
    {
        return Collections.emptySet();
    }

    protected Set<MarkedRule> calculateClosure(Set<MarkedRule> rules)
    {
        Set<MarkedRule> closure = new HashSet<MarkedRule>(rules);

        return closure;
    }

    protected static <T> Set<T> asSet(T... elements)
    {
        return new HashSet<T>(Arrays.asList(elements));
    }
}
