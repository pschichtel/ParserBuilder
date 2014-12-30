package de.cubeisland.engine.parser;

import java.util.Map;
import de.cubeisland.engine.parser.action.*;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

import static de.cubeisland.engine.parser.action.Error.ERROR;

public class ActionTable
{
    private final Map<Entry, Action> actions;

    public ActionTable(Map<Entry, Action> actions)
    {
        this.actions = actions;
    }

    public Action resolve(ParseState state, TokenSpec token)
    {
        Action action = this.actions.get(new Entry(state, token));
        if (action == null)
        {
            return ERROR;
        }
        return action;
    }

    public static final class Entry
    {
        public final ParseState state;
        public final TokenSpec spec;

        public Entry(ParseState state, TokenSpec spec)
        {
            this.state = state;
            this.spec = spec;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            final Entry entry = (Entry)o;

            if (spec != null ? !spec.equals(entry.spec) : entry.spec != null)
            {
                return false;
            }
            if (state != null ? !state.equals(entry.state) : entry.state != null)
            {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            int result = state != null ? state.hashCode() : 0;
            result = 31 * result + (spec != null ? spec.hashCode() : 0);
            return result;
        }
    }
}
