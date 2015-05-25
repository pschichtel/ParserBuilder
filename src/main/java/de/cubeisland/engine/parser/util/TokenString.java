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
package de.cubeisland.engine.parser.util;

import de.cubeisland.engine.parser.rule.token.TokenClass;

import java.util.*;

import static de.cubeisland.engine.parser.Util.asSet;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public class TokenString implements Iterable<TokenClass>
{
    public static final TokenString EMPTY = new TokenString(Collections.<TokenClass>emptyList());
    private static final Set<TokenString> EMPTY_SET = asSet(EMPTY);

    private final List<TokenClass> tokens;

    private TokenString(List<TokenClass> tokens)
    {
        this.tokens = unmodifiableList(tokens);
    }

    public TokenClass tokenAt(int i)
    {
        return this.tokens.get(i);
    }

    public Iterator<TokenClass> iterator()
    {
        return this.tokens.iterator();
    }

    public int size()
    {
        return this.tokens.size();
    }

    public TokenString substring(int begin, int end) {
        return new TokenString(this.tokens.subList(begin, end));
    }

    public TokenString maximumSubstring(int begin, int end) {
        final int max = size();
        return substring(begin, end < max ? end : max);
    }

    public TokenString concat(int k, TokenString other)
    {
        List<TokenClass> concatenatedString = new ArrayList<TokenClass>(this.tokens);
        concatenatedString.addAll(other.tokens);
        final int length = concatenatedString.size();
        return new TokenString(concatenatedString.subList(0, k < length ? k : length));
    }

    public static Set<TokenString> concatMany(int k, Set<TokenString> m, Set<TokenString> n)
    {
        Set<TokenString> result = new HashSet<TokenString>();
        if (m.isEmpty())
        {
            // we have to concat with something for this to work, so we use a set containing just the empty string
            m = EMPTY_SET;
        }
        for (TokenString v : m)
        {
            for (TokenString w : n)
            {
                result.add(v.concat(k, w));
            }
        }
        return result;
    }

    public static Set<TokenString> concatMany(int k, Set<TokenString>... tokenLists)
    {
        Set<TokenString> result = EMPTY_SET;

        for (Set<TokenString> m : tokenLists)
        {
            result = concatMany(k, result, m);
        }

        return result;
    }

    public static TokenString str(List<TokenClass> tokens)
    {
        if (tokens.isEmpty())
        {
            return EMPTY;
        }
        return new TokenString(tokens);
    }

    public static TokenString str(TokenClass... tokens)
    {
        if (tokens.length == 0)
        {
            return EMPTY;
        }
        return str(asList(tokens));
    }

    @Override
    public int hashCode()
    {
        return this.tokens.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (this == obj)
        {
            return true;
        }
        if (obj instanceof TokenString)
        {
            return this.tokens.equals(((TokenString)obj).tokens);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return this.tokens.toString();
    }
}
