package de.cubeisland.engine.parser.rule.token.tokenizer;

import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.Token;
import de.cubeisland.engine.parser.rule.token.InputSource;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
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
        TokenSpec A = simple("aa");
        TokenSpec B = simple("b");
        Variable x = new Variable("x");

        Grammar g = Grammar
                .with(x.produces(A, B).skip())
                .with(x.produces(B, A).skip())
                .startingWith(x);

        AutomateTokenizer t = AutomateTokenizer.fromGrammar(g);

        InputSource s = new CharSequenceSource("aaaabbbb");

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
