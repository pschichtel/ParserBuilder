package de.cubeisland.engine.parser.rule.token.automate.action;

import de.cubeisland.engine.parser.rule.token.*;

public class IntegerAction implements TokenAction {
    @Override
    public Token act(TokenSpec spec, String s) {
        // TODO proper typing
        return new ParametrizedToken<Integer>((ParametrizedTokenSpec<Integer>) spec, Integer.parseInt(s));
    }
}
