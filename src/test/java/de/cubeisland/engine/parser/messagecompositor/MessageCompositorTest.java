package de.cubeisland.engine.parser.messagecompositor;

import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenSpec;
import de.cubeisland.engine.parser.rule.token.SimpleTokenSpec;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import de.cubeisland.engine.parser.rule.token.automate.action.TokenActions;
import org.junit.Before;
import org.junit.Test;

import static de.cubeisland.engine.parser.rule.Reaction.SkipReaction.SKIP;
import static de.cubeisland.engine.parser.rule.Rule.head;
import static de.cubeisland.engine.parser.rule.token.Epsilon.EPSILON;
import static de.cubeisland.engine.parser.util.PrintingUtil.printTokenStringMap;
import static java.util.regex.Pattern.compile;

public class MessageCompositorTest {

    private static final TokenSpec NUMBER = new ParametrizedTokenSpec<Integer>("NUMBER", compile("0|[1-9][0-9]*"), TokenActions.INTEGER);
    private static final TokenSpec ID = new ParametrizedTokenSpec<String>("ID", compile("[a-zA-Z]+"), TokenActions.STRING);
    private static final TokenSpec NAME = new ParametrizedTokenSpec<String>("NAME", compile("[a-z][a-z0-9\\- _]+"), TokenActions.STRING);
    private static final TokenSpec VALUE = new ParametrizedTokenSpec<String>("VALUE", compile("[^,]+"), TokenActions.STRING);
    private static final TokenSpec TEXT = new ParametrizedTokenSpec<String>("TEXT", compile(".+"), TokenActions.STRING);
    private static final TokenSpec LABEL = new ParametrizedTokenSpec<String>("LABEL", compile("[^:\\}]+"), TokenActions.STRING);

    private static final TokenSpec BEGIN_MACRO = new SimpleTokenSpec("{");
    private static final TokenSpec END_MACRO = new SimpleTokenSpec("}");
    private static final TokenSpec SEGMENT_SEPARATOR = new SimpleTokenSpec(":");
    private static final TokenSpec LABEL_SEPARATOR = new SimpleTokenSpec("#");
    private static final TokenSpec ARGUMENT_SEPARATOR = new SimpleTokenSpec(",");
    private static final TokenSpec VALUE_SEPARATOR = new SimpleTokenSpec("=");

    private static final Variable message = new Variable("message");
    private static final Variable text = new Variable("text");
    private static final Variable macro = new Variable("macro");
    private static final Variable rule = new Variable("rule");
    private static final Variable name = new Variable("name");
    private static final Variable args = new Variable("args");
    private static final Variable arg = new Variable("arg");
    private static final Variable named = new Variable("named");

    Grammar g;

    @Before
    public void setUp() throws Exception {
        g = Grammar
                .with(head(message).produces(message, macro, text).reactingWith(SKIP))
                .with(head(message).produces(text).reactingWith(SKIP))
                .with(head(text).produces(TEXT).reactingWith(SKIP))
                .with(head(text).produces(EPSILON).reactingWith(SKIP))
                .with(head(macro).produces(BEGIN_MACRO, rule, END_MACRO).reactingWith(SKIP))
                .with(head(rule).produces(NUMBER, SEGMENT_SEPARATOR, name, SEGMENT_SEPARATOR, args).reactingWith(SKIP))
                .with(head(rule).produces(NUMBER, SEGMENT_SEPARATOR, name).reactingWith(SKIP))
                .with(head(rule).produces(NUMBER).reactingWith(SKIP))
                .with(head(rule).produces(EPSILON).reactingWith(SKIP))
                .with(head(name).produces(ID, LABEL_SEPARATOR, LABEL).reactingWith(SKIP))
                .with(head(name).produces(ID).reactingWith(SKIP))
                .with(head(args).produces(arg, ARGUMENT_SEPARATOR, args).reactingWith(SKIP))
                .with(head(args).produces(arg).reactingWith(SKIP))
                .with(head(arg).produces(named).reactingWith(SKIP))
                .with(head(arg).produces(VALUE).reactingWith(SKIP))
                .with(head(named).produces(NAME, VALUE_SEPARATOR, VALUE).reactingWith(SKIP))
                .startingWith(message);
    }

    @Test
    public void testGrammar() {
        System.out.println(g.augment());
        printTokenStringMap(g.first());
        printTokenStringMap(g.follow());
    }
}
