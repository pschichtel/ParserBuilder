package de.cubeisland.engine.parser;

import java.util.List;
import java.util.Set;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.token.TokenSpec;

public class CompiledGrammar extends Grammar
{
    private final GotoTable gotoTable;
    private final ActionTable actionTable;

    public CompiledGrammar(Set<Variable> variable, Set<TokenSpec> tokens, List<Rule> rules, Rule startRule, GotoTable gotos, ActionTable actions)
    {
        super(variable, tokens, rules, startRule);
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
