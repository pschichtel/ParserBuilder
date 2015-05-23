package de.cubeisland.engine.parser.rule.token;

import de.cubeisland.engine.parser.rule.token.automate.action.TokenAction;

public abstract class ReadableTokenSpec extends TokenSpec
{
    private final TokenAction action;

    public ReadableTokenSpec(String name, TokenAction action)
    {
        super(name);
        this.action = action;
    }

    public TokenAction getAction()
    {
        return this.action;
    }

    @Override
    public String toString() {
        return getName() + " ↦ ";
    }
}
