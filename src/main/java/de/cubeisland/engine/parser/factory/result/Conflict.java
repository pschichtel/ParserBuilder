package de.cubeisland.engine.parser.factory.result;

import de.cubeisland.engine.parser.ParseState;
import de.cubeisland.engine.parser.action.Action;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

public class Conflict
{
    private final Type type;
    private final ParseState state;
    private final TokenSpec token;
    private final Action conflictingAction;

    public Conflict(Type type, ParseState state, TokenSpec token, Action conflictingAction)
    {
        this.type = type;
        this.state = state;
        this.token = token;
        this.conflictingAction = conflictingAction;
    }

    public Type getType()
    {
        return type;
    }

    public ParseState getState()
    {
        return state;
    }

    public TokenSpec getToken()
    {
        return token;
    }

    public Action getConflictingAction()
    {
        return conflictingAction;
    }

    public static enum Type
    {
        SHIFT_SHIFT,
        SHIFT_REDUCE
    }
}
