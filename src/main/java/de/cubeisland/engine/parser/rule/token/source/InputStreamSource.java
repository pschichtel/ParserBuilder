package de.cubeisland.engine.parser.rule.token.source;

import de.cubeisland.engine.parser.rule.token.InputSource;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamSource implements InputSource, Closeable
{
    private final InputStream stream;
    private int next;

    public InputStreamSource(InputStream stream) throws IOException
    {
        this.stream = stream;
        this.next = stream.read();
    }

    @Override
    public boolean isDepleted()
    {
        return this.next >= 0;
    }

    @Override
    public char read() throws IOException
    {
        final char out = (char)this.next;
        this.next = this.stream.read();
        return out;
    }

    @Override
    public void close() throws IOException
    {
        this.stream.close();
    }
}
