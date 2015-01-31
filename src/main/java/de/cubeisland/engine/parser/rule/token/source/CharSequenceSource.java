package de.cubeisland.engine.parser.rule.token.source;

import de.cubeisland.engine.parser.rule.token.InputSource;

public class CharSequenceSource implements InputSource
{
    private final CharSequence seq;
    private int offset;

    public CharSequenceSource(CharSequence seq)
    {
        this.seq = seq;
        this.offset = 0;
    }

    @Override
    public boolean isDepleted()
    {
        return this.offset >= this.seq.length();
    }

    @Override
    public char read()
    {
        return this.seq.charAt(this.offset++);
    }
}
