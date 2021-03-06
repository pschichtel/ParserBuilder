/**
 * The MIT License
 * Copyright (c) 2014 Cube Island
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.cubeisland.engine.parser;

import java.util.Map;
import de.cubeisland.engine.parser.action.Action;
import de.cubeisland.engine.parser.parser.ParseState;
import de.cubeisland.engine.parser.rule.token.TokenClass;

import static de.cubeisland.engine.parser.action.Error.ERROR;

public class ActionTable
{
    private final Map<Entry, Action> actions;

    public ActionTable(Map<Entry, Action> actions)
    {
        this.actions = actions;
    }

    public Action resolve(ParseState state, TokenClass tokenClass)
    {
        Action action = this.actions.get(new Entry(state, tokenClass));
        if (action == null)
        {
            return ERROR;
        }
        return action;
    }

    public static final class Entry
    {
        public final ParseState state;
        public final TokenClass token;

        public Entry(ParseState state, TokenClass token)
        {
            this.state = state;
            this.token = token;
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

            if (token != null ? !token.equals(entry.token) : entry.token != null)
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
            result = 31 * result + (token != null ? token.hashCode() : 0);
            return result;
        }
    }
}
