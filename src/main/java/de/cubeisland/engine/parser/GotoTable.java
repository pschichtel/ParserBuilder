package de.cubeisland.engine.parser;

import java.util.Map;
import de.cubeisland.engine.parser.rule.RuleElement;

public class GotoTable
{
    private final Map<Entry, ParseState> gotos;

    public GotoTable(Map<Entry, ParseState> gotos)
    {
        this.gotos = gotos;
    }

    public ParseState resolve(ParseState currentState, RuleElement element)
    {
        return this.gotos.get(new Entry(currentState, element));
    }

    public static final class Entry
    {
        public final ParseState state;
        public final RuleElement element;

        public Entry(ParseState state, RuleElement element)
        {
            this.state = state;
            this.element = element;
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

            if (element != null ? !element.equals(entry.element) : entry.element != null)
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
            result = 31 * result + (element != null ? element.hashCode() : 0);
            return result;
        }
    }
}
