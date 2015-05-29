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
package de.cubeisland.engine.parser.rule.token.source;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import de.cubeisland.engine.parser.rule.token.CharacterStream;
import de.cubeisland.engine.parser.rule.token.CharacterStream.SourceDepletedException;
import de.cubeisland.engine.parser.rule.token.InputSource;

public class InputStreamSource implements InputSource, Closeable
{
    private final InputStream stream;
    boolean hasNext = false;
    private int next = 0;

    public InputStreamSource(InputStream stream) throws IOException
    {
        this.stream = stream;
    }

    @Override
    public boolean isDepleted() throws IOException
    {
        if (!hasNext)
        {
            this.next = stream.read();
            hasNext = true;
        }
        return this.next < 0;
    }

    @Override
    public char read() throws IOException, SourceDepletedException
    {
        if (isDepleted())
        {
            throw new SourceDepletedException("There is nothing more to read from this InputStream");
        }

        this.hasNext = false;
        return (char)this.next;
    }

    @Override
    public CharacterStream stream()
    {
        return new CharacterStream(this);
    }

    @Override
    public Iterator<Character> iterator()
    {
        return stream();
    }

    @Override
    public void close() throws IOException
    {
        this.stream.close();
    }
}
