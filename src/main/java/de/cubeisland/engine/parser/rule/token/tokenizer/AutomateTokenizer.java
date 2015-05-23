package de.cubeisland.engine.parser.rule.token.tokenizer;

import de.cubeisland.engine.parser.grammar.BaseGrammar;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenSpec;
import de.cubeisland.engine.parser.rule.token.SimpleToken;
import de.cubeisland.engine.parser.rule.token.SimpleTokenSpec;
import de.cubeisland.engine.parser.rule.token.Token;
import de.cubeisland.engine.parser.rule.token.InputSource;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import de.cubeisland.engine.parser.rule.token.Tokenizer;
import de.cubeisland.engine.parser.rule.token.automate.DFA;
import de.cubeisland.engine.parser.rule.token.automate.FiniteAutomate;
import de.cubeisland.engine.parser.rule.token.automate.Matcher;
import de.cubeisland.engine.parser.rule.token.automate.NFA;
import de.cubeisland.engine.parser.rule.token.automate.State;
import de.cubeisland.engine.parser.rule.token.automate.Transition;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

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

    @Override
    public Token nextToken(InputSource source) throws IOException
    {
        if (source.isDepleted())
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
                this.currentChar = source.read();
            }
            next = currentState.transition(this.automate, this.currentChar);
            if (next != ERROR)
            {
                currentState = next;
                string.append(this.currentChar);
                this.currentChar = null;
            }
        }
        while (next != ERROR && !source.isDepleted());


        if (!this.automate.isAccepting(currentState))
        {
            this.currentChar = null;
            throw new IllegalStateException("Invalid input!");
        }

        System.out.println("###############");
        return new SimpleToken(new SimpleTokenSpec(string.toString()));
    }

    public static AutomateTokenizer fromGrammar(BaseGrammar g)
    {
        final Set<TokenSpec> tokens = g.getTokens();

        Iterator<TokenSpec> it = tokens.iterator();
        if (it.hasNext())
        {
            FiniteAutomate<? extends Transition> automate = specToAutomate(it.next());

            while (it.hasNext())
            {
                automate = automate.or(specToAutomate(it.next()));
            }
            return new AutomateTokenizer(automate.toDFA().minimize());
        }

        throw new IllegalArgumentException("The given grammar does not define any tokens!");
    }

    private static FiniteAutomate<? extends Transition> specToAutomate(TokenSpec p)
    {
        if (p instanceof SimpleTokenSpec)
        {
            return Matcher.matchAll(p.getName().toCharArray());
        }
        else if (p instanceof ParametrizedTokenSpec<?>)
        {
            return Matcher.match(((ParametrizedTokenSpec) p).getPattern());
        }
        return NFA.EPSILON;
    }
}
