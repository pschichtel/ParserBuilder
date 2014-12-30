package de.cubeisland.engine.parser.grammar;

import java.util.Set;
import de.cubeisland.engine.parser.ActionTable;
import de.cubeisland.engine.parser.GotoTable;
import de.cubeisland.engine.parser.ParseState;

public class CompiledGrammar extends BaseGrammar
{
    private final Set<ParseState> states;
    private final GotoTable gotoTable;
    private final ActionTable actionTable;


    public CompiledGrammar(AugmentedGrammar g, Set<ParseState> states, GotoTable gotos, ActionTable actions)
    {
        super(g.getVariables(), g.getTokens(), g.getRules(), g.getStart());
        this.states = states;
        this.gotoTable = gotos;
        this.actionTable = actions;
    }

    public Set<ParseState> getStates()
    {
        return states;
    }

    public GotoTable getGotoTable()
    {
        return gotoTable;
    }

    public ActionTable getActionTable()
    {
        return actionTable;
    }
}
