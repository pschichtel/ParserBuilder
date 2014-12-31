package de.cubeisland.engine.parser.type;

import java.util.Set;
import de.cubeisland.engine.parser.ParseState;
import de.cubeisland.engine.parser.TestGrammars;
import de.cubeisland.engine.parser.TestGrammars.SimpleExpr;
import de.cubeisland.engine.parser.grammar.AugmentedGrammar;
import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.Rule.MarkedRule;
import org.junit.Test;

import static de.cubeisland.engine.parser.Util.asSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LRTypeTest
{
    Grammar g = TestGrammars.SIMPLE_EXPR;
    AugmentedGrammar a = g.augment();
    LRType lr = new LRType();

    @Test
    public void testClosure() throws Exception
    {
        Set<MarkedRule> closure = lr.closure(a, asSet(a.getStartRule().mark()));

        assertThat("LRType.closure() did not find all productions", closure.size(), is(9));
    }

    @Test
    public void testReadElement() throws Exception
    {
        ParseState initial = lr.calculateInitialState(a);
        ParseState newState = lr.goTo(a, initial, SimpleExpr.BGN);
        System.out.println(newState);

        assertThat("LRType.goTo() did not find all productions", newState.getRules().size(), is(9));
    }
}
