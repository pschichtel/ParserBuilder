package de.cubeisland.engine.parser.rule.token.automate;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReadTest
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
        DFA a = Read.read('a');

        printAutomate("Read char", a);
    }

    @Test
    public void testAnd() throws Exception
    {
        DFA a = Read.read('a');
        DFA b = Read.read('b');

        NFA c = Read.and(a, b);
        printAutomate("And", c);
    }

    @Test
    public void testOr() throws Exception
    {
        DFA a = Read.read('a');
        DFA b = Read.read('b');

        NFA c = Read.and(a, b);
        printAutomate("Or", c);
    }

    @Test
    public void testKleene() throws Exception
    {
        DFA a = Read.read('a');

        NFA b = Read.kleene(a);
        printAutomate("Kleene", b);
    }
}