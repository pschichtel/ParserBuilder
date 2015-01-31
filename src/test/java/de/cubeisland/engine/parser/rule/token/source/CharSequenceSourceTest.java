package de.cubeisland.engine.parser.rule.token.source;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharSequenceSourceTest
{
    @Test
    public void testRead() throws Exception
    {
        CharSequenceSource s = new CharSequenceSource("aaaabbbb");

        while (!s.isDepleted())
        {
            System.out.println(s.read());
        }
    }
}