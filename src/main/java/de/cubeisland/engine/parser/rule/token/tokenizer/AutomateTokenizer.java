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
package de.cubeisland.engine.parser.rule.token.tokenizer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import de.cubeisland.engine.parser.grammar.BaseGrammar;
import de.cubeisland.engine.parser.rule.token.CharacterStream;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenClass;
import de.cubeisland.engine.parser.rule.token.SimpleToken;
import de.cubeisland.engine.parser.rule.token.SimpleTokenClass;
import de.cubeisland.engine.parser.rule.token.Token;
import de.cubeisland.engine.parser.rule.token.TokenClass;
import de.cubeisland.engine.parser.rule.token.Tokenizer;
import de.cubeisland.engine.parser.rule.token.automate.DFA;
import de.cubeisland.engine.parser.rule.token.automate.FiniteAutomate;
import de.cubeisland.engine.parser.rule.token.automate.match.Matcher;
import de.cubeisland.engine.parser.rule.token.automate.NFA;
import de.cubeisland.engine.parser.rule.token.automate.State;
import de.cubeisland.engine.parser.rule.token.automate.transition.Transition;

import static de.cubeisland.engine.parser.rule.token.EndOfFileToken.EOF;
import static de.cubeisland.engine.parser.rule.token.automate.ErrorState.ERROR;

public class AutomateTokenizer implements Tokenizer
{
    private final DFA automate;
    private Character currentChar;

    public AutomateTokenizer(DFA automate)
    {
        this.automate = automate;
    }

    public static AutomateTokenizer fromGrammar(BaseGrammar g)
    {
        final Set<TokenClass> tokens = g.getTokens();

        Iterator<TokenClass> it = tokens.iterator();
        if (!it.hasNext())
        {
            throw new IllegalArgumentException("The given grammar does not define any tokens!");
        }

        FiniteAutomate<? extends Transition> automate = tokenClassToAutomate(it.next());
        while (it.hasNext())
        {
            automate = automate.or(tokenClassToAutomate(it.next()));
        }
        return new AutomateTokenizer(automate.toDFA().minimize());
    }

    @Override
    public Token nextToken(CharacterStream input) throws IOException
    {
        if (input.isDepleted())
        {
            return EOF;
        }

        State currentState = automate.getStartState();
        final StringBuilder string = new StringBuilder();
        State next;
        do
        {
            if (this.currentChar == null)
            {
                System.out.println("read");
                this.currentChar = input.next();
            }
            next = currentState.transition(this.automate, this.currentChar);
            if (next != ERROR)
            {
                currentState = next;
                string.append(this.currentChar);
                this.currentChar = null;
            }
        }
        while (next != ERROR && !input.isDepleted());

        if (!this.automate.isAccepting(currentState))
        {
            this.currentChar = null;
            throw new IllegalStateException("Invalid input!");
        }

        System.out.println("###############");
        return new SimpleToken(new SimpleTokenClass(string.toString()));
    }

    private static FiniteAutomate<? extends Transition> tokenClassToAutomate(TokenClass p)
    {
        if (p instanceof SimpleTokenClass)
        {
            return Matcher.matchAll(p.getName().toCharArray());
        }
        else if (p instanceof ParametrizedTokenClass<?>)
        {
            return Matcher.match(((ParametrizedTokenClass)p).getPattern());
        }
        return NFA.EPSILON;
    }
}
