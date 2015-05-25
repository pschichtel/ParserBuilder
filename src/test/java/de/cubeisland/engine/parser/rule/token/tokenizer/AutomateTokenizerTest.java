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

import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.CharacterStream;
import de.cubeisland.engine.parser.rule.token.Token;
import de.cubeisland.engine.parser.rule.token.TokenClass;
import de.cubeisland.engine.parser.rule.token.source.CharSequenceSource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static de.cubeisland.engine.parser.rule.token.EndOfFileToken.EOF;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.simple;

public class AutomateTokenizerTest
{
    @Test
    public void testTokenize() throws Exception
    {
        TokenClass A = simple("aa");
        TokenClass B = simple("b");
        Variable x = new Variable("x");

        Grammar g = Grammar
                .with(x.produces(A, B).skip())
                .with(x.produces(B, A).skip())
                .startingWith(x);

        AutomateTokenizer t = AutomateTokenizer.fromGrammar(g);

        CharacterStream s = new CharSequenceSource("aaaabbbb").stream();

        List<Token> tokens = new ArrayList<Token>();
        Token token;
        do
        {
            token = t.nextToken(s);
            tokens.add(token);
        }
        while (token != EOF);

        for (Token tok : tokens)
        {
            System.out.print(tok.getName() + " ");
        }
    }
}
