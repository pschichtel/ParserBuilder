package de.cubeisland.engine.parser.util;

import de.cubeisland.engine.parser.rule.token.automate.DFA;
import de.cubeisland.engine.parser.rule.token.automate.FiniteAutomate;
import de.cubeisland.engine.parser.rule.token.automate.NFA;
import de.cubeisland.engine.parser.rule.token.automate.Transition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import static de.cubeisland.engine.parser.Util.convertCharCollectionToArray;
import static de.cubeisland.engine.parser.rule.token.automate.Matcher.matchOne;
import static de.cubeisland.engine.parser.rule.token.automate.NFA.EPSILON;

public class PatternParser
{

    public static DFA toDFA(Pattern pattern)
    {
        return toNFA(pattern).toDFA();
    }

    public static NFA toNFA(Pattern pattern)
    {
        return readExpression(new StringStream(pattern.toString()), 0);
    }

    private static NFA readExpression(StringStream s, int depth) {
        LinkedList<FiniteAutomate<? extends Transition>> elements = new LinkedList<FiniteAutomate<? extends Transition>>();

        for (final char c : s)
        {
            switch (c)
            {
                case '[':
                    elements.addLast(readCharacterClass(s));
                    break;
                case '(':
                    elements.addLast(readExpression(s, depth + 1));
                    break;
                case ')':
                    if (depth > 0)
                    {
                        break;
                    }
                case '+':
                case '*':
                case '{':
                case '?':
                    if (!elements.isEmpty())
                    {
                        elements.addLast(readQuantifier(s, elements.removeLast()));
                    }
                    else
                    {
                        elements.addLast(readCharacter(s));
                    }
                    break;
                case '.':
                    // TODO wildcard match
                default:
                    elements.addLast(readCharacter(s));
            }
        }

        if (elements.isEmpty())
        {
            return NFA.EMPTY;
        }

        NFA automate = elements.getFirst().toNFA();
        for (final FiniteAutomate<? extends Transition> element : elements.subList(1, elements.size()))
        {
            automate = automate.and(element);
        }

        return automate;
    }

    public static NFA readQuantifier(StringStream s, FiniteAutomate<? extends Transition> automate)
    {
        switch (s.current())
        {
            case '*':
                return automate.kleene();
            case '+':
                return automate.and(automate.kleene());
            case '?':
                return automate.or(EPSILON);
            case '{':
                return readSpecificQuantifier(s, automate);
            default:
                return automate.toNFA();
        }
    }

    private static NFA readSpecificQuantifier(StringStream s, FiniteAutomate<? extends Transition> automate)
    {
        s.store();

        if (s.canPeekAhead() && Character.isDigit(s.peekAhead()))
        {
            int atLeast = readNumber(s);
            char c = s.current();

            if (c == '}')
            {
                s.drop();
                return automate.repeat(atLeast);
            }
            else if (c == ',' && s.canPeekAhead() && Character.isDigit(s.peekAhead()))
            {
                int atMost = readNumber(s);
                if (s.current() == '}')
                {
                    s.drop().skip();
                    return automate.range(atLeast, atMost);
                }
            }
        }

        s.pop();

        return automate.and(readCharacter(s));
    }

    private static int readNumber(StringStream s)
    {
        int out = 0;
        for (final char c : s)
        {
            if (Character.isDigit(c))
            {
                out = out * 10 + (c - '0');
            }
        }

        return out;
    }

    private static DFA readCharacterClass(StringStream s)
    {
        NFA automate = NFA.EMPTY;

        if (!s.canPeekAhead(2))
        {
            return readCharacter(s);
        }
        boolean negative = s.peekAhead() == '^';

        s.store();
        if (negative)
        {
            s.skip();
        }
        if (s.peekAhead() == ']')
        {
            s.pop();
            return readCharacter(s);
        }

        boolean hasEnded = false;
        for (final char c : s)
        {
            if (c == ']')
            {
                hasEnded = true;
                break;
            }
            automate = automate.or(readCharacter(s));
        }

        if (!hasEnded)
        {
            s.pop();
            return readCharacter(s);
        }
        s.drop();
        if (negative)
        {
            // TODO verify this
            automate = automate.complement();
        }
        return automate.toDFA();
    }

    private static DFA readCharacter(StringStream s)
    {
        char c = s.current();
        if (c == '\\')
        {
            return readEscapeSequence(s);
        }
        return matchOne(c);
    }

    private static DFA readEscapeSequence(StringStream s)
    {
        switch (s.next())
        {
            case 'n':
                return matchOne('\n');
            case 'r':
                return matchOne('\r');
            case 's':
                return matchOne(' ', '\t', '\n', '\r');
            case '\\':
                return matchOne('\\');
            case 'd':
                return matchOne('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
            case 'w':
                List<Character> chars = new ArrayList<Character>();
                NFA automate = NFA.EMPTY;
                for (int i = 'a'; i <= 'z'; ++i)
                {
                    chars.add((char)i);
                }
                for (int i = 'A'; i <= 'Z'; ++i)
                {
                    chars.add((char)i);
                }
                chars.add('_');
                return matchOne(convertCharCollectionToArray(chars));
            default:
                return DFA.EMPTY;
        }
    }

    private static final class StringStream implements Iterator<Character>, Iterable<Character>
    {
        private final String s;
        private volatile int offset = -1;
        private Stack<Integer> offsetStack = new Stack<Integer>();

        public StringStream(String s)
        {
            this.s = s;
        }

        @Override
        public Iterator<Character> iterator()
        {
            return this;
        }

        @Override
        public boolean hasNext()
        {
            return offset + 1 < s.length();
        }

        @Override
        public Character next()
        {
            if (!hasNext())
            {
                throw new StringIndexOutOfBoundsException("String is depleted");
            }
            return s.charAt(++offset);
        }

        public boolean canPeekAhead()
        {
            return canPeekAhead(1);
        }

        public boolean canPeekAhead(int n)
        {
            return offset + n < s.length();
        }

        public char peekAhead()
        {
            return peekAhead(1);
        }

        public char peekAhead(int n)
        {
            if (!canPeekAhead(n))
            {
                throw new StringIndexOutOfBoundsException("Can't peek that far: " + n);
            }
            return s.charAt(offset + n);
        }

        public char current()
        {
            if (offset < 0)
            {
                throw new IllegalStateException("You can't call current() before invoking next() at least once!");
            }
            return s.charAt(offset);
        }

        @Override
        public void remove()
        {
            skip();
        }

        public StringStream skip()
        {
            if (hasNext())
            {
                offset++;
            }
            return this;
        }

        public boolean isAtStart()
        {
            return offset == 0;
        }

        public boolean isAtEnd()
        {
            return !canPeekAhead();
        }

        public StringStream store()
        {
            offsetStack.push(offset);
            return this;
        }

        public StringStream pop()
        {
            if (!offsetStack.empty())
            {
                offset = offsetStack.pop();
            }
            return this;
        }

        public StringStream drop()
        {
            if (!offsetStack.empty())
            {
                offsetStack.pop();
            }
            return this;
        }
    }
}
