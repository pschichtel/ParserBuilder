package de.cubeisland.engine.parser;

import java.util.List;

public class CompiledGrammar extends Grammar
{
    private final GotoTable gotoTable;
    private final ActionTable actionTable;

    public CompiledGrammar(List<Rule> rules, GotoTable gotos, ActionTable actions)
    {
        super(rules);
        this.gotoTable = gotos;
        this.actionTable = actions;
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
