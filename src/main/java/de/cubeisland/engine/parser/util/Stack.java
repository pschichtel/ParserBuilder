package de.cubeisland.engine.parser.util;

import java.util.ArrayList;
import java.util.List;

public class Stack<T>
{
    private static final Stack<Object> NIL = new Nil();

    private final T head;
    private final Stack<T> tail;

    public Stack(T head)
    {
        this(head, Stack.<T>nil());
    }

    public Stack(T head, Stack<T> tail)
    {
        this.head = head;
        this.tail = tail;
    }

    public T peek()
    {
        return this.head;
    }

    public List<T> peek(int n)
    {
        return this.peek(n, new ArrayList<T>());
    }

    private List<T> peek(int n, List<T> peeked)
    {
        if (n > 0)
        {
            peeked.add(this.head);
            if (this.tail != null)
            {
                this.tail.peek(n - 1, peeked);
            }
        }
        return peeked;
    }

    public Stack<T> pop()
    {
        return this.tail;
    }

    public Stack<T> pop(int n)
    {
        if (n == 0)
        {
            return this;
        }
        if (this.tail == null)
        {
            return null;
        }
        return this.pop(n - 1);
    }

    public Stack<T> push(T value)
    {
        return new Stack<T>(value, this);
    }

    public int size()
    {
        return 1 + tail.size();
    }

    @SuppressWarnings("unchecked")
    public static <T> Stack<T> nil()
    {
        return (Stack<T>)NIL;
    }

    public static class Nil extends Stack<Object>
    {
        public Nil()
        {
            super(null, null);
        }

        @Override
        public Object peek()
        {
            throw new UnsupportedOperationException("No elements in Nil!");
        }

        @Override
        public List<Object> peek(int n)
        {
            throw new UnsupportedOperationException("No elements in Nil!");
        }

        @Override
        public Stack<Object> pop()
        {
            return this;
        }

        @Override
        public Stack<Object> pop(int n)
        {
            return this;
        }

        @Override
        public int size()
        {
            return 0;
        }
    }
}
