package de.cubeisland.engine.parser.rule.token.automate.action;

import de.cubeisland.engine.parser.rule.token.ParametrizedToken;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenSpec;
import de.cubeisland.engine.parser.rule.token.Token;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

public class StringAction implements TokenAction {
    @Override
    public Token act(TokenSpec spec, String s) {
        return new ParametrizedToken<String>((ParametrizedTokenSpec<String>) spec, s);
    }
}
