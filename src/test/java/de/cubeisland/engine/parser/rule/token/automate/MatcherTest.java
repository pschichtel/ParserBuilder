package de.cubeisland.engine.parser.rule.token.automate;

import org.junit.Test;

import static de.cubeisland.engine.parser.rule.token.automate.Matcher.match;

public class MatcherTest
{

    protected static void printAutomate(String name, FiniteAutomate<? extends Transition> a)
    {
        System.out.println(name + ":");
        System.out.println("States:      " + a.getStates());
        System.out.println("Transitions: " + a.getTransitions());
        System.out.println("Accepting:   " + a.getAcceptingStates());
        System.out.println("Start:       " + a.getStartState());
        System.out.println();
    }

    @Test
    public void testRead() throws Exception
    {
        DFA a = match('a');

        printAutomate("Read char", a);
    }

    @Test
    public void testAnd() throws Exception
    {
        DFA a = match('a');
        DFA b = match('b');

        NFA c = a.and(b);
        printAutomate("And", c);
    }

    @Test
    public void testOr() throws Exception
    {
        DFA a = match('a');
        DFA b = match('b');

        NFA c = a.and(b);
        printAutomate("Or", c);
    }

    @Test
    public void testKleene() throws Exception
    {
        DFA a = match('a');

        NFA b = a.kleene();
        printAutomate("Kleene", b);
    }
}