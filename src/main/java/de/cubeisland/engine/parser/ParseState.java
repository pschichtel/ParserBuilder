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

import java.util.Set;
import de.cubeisland.engine.parser.rule.Rule.MarkedRule;

import static java.util.Collections.unmodifiableSet;

public class ParseState extends Identified
{
    private final Set<MarkedRule> rules;

    public ParseState(Set<MarkedRule> rules)
    {
        this.rules = unmodifiableSet(rules);
    }

    public Set<MarkedRule> getRules()
    {
        return rules;
    }

    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder("s").append(getId()).append("(\n");
        for (final MarkedRule rule : rules)
        {
            out.append('\t').append(rule).append('\n');
        }
        out.append(')');
        return out.toString();
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
        if (!super.equals(o))
        {
            return false;
        }

        final ParseState that = (ParseState)o;

        if (!rules.equals(that.rules))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
