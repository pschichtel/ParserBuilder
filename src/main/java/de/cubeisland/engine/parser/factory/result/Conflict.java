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
package de.cubeisland.engine.parser.factory.result;

import de.cubeisland.engine.parser.action.Action;
import de.cubeisland.engine.parser.parser.ParseState;
import de.cubeisland.engine.parser.rule.token.TokenClass;

public class Conflict
{
    private final Type type;
    private final ParseState state;
    private final TokenClass token;
    private final Action conflictingAction;

    public Conflict(Type type, ParseState state, TokenClass token, Action conflictingAction)
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

    public TokenClass getToken()
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
