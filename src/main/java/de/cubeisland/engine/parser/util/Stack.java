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
