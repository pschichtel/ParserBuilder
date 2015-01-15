package de.cubeisland.engine.parser.rule.token.automate;

import org.junit.Test;

import static de.cubeisland.engine.parser.rule.token.automate.Matcher.match;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DFATest
{
    @Test
    public void testGetBy() throws Exception
    {
        String string  = "Test123";
        String string2 = "super";
        DFA a = match(string).or(match(string2)).toDFA();

        State s = a.getStartState();
        for (char c : string.toCharArray())
        {
            System.out.println("Current state: " + s);
            s = s.transition(a, c);
        }

        assertThat("String was not matched!", a.isAccepting(s), is(true));

        s = a.getStartState();
        for (char c : string2.toCharArray())
        {
            System.out.println("Current state: " + s);
            s = s.transition(a, c);
        }

        assertThat("String2 was not matched!", a.isAccepting(s), is(true));
    }

    @Test
    public void testMinimize() throws Exception
    {

    }
}