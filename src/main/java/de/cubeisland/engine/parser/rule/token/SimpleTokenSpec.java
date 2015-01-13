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
package de.cubeisland.engine.parser.rule.token;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;

public class SimpleTokenSpec extends TokenSpec
{
    private final Pattern pattern;
    private final String output;

    public SimpleTokenSpec(String name, String pattern)
    {
        this(name, compile(quote(pattern)), pattern);
    }

    public SimpleTokenSpec(String name, Pattern pattern)
    {
        this(name, pattern, "Regex(" + pattern.toString() + ")");
    }

    private SimpleTokenSpec(String name, Pattern pattern, String output)
    {
        super(name);
        this.pattern = pattern;
        this.output = output;
    }

    public Pattern getPattern()
    {
        return pattern;
    }

    @Override
    public String toString()
    {
        return getName() + " ↦ " + this.output;
    }
}
