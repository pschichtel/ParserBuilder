package de.cubeisland.engine.parser.messagecompositor;

import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenSpec;
import de.cubeisland.engine.parser.rule.token.SimpleTokenSpec;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

import static de.cubeisland.engine.parser.rule.Reaction.SkipReaction.SKIP;
import static de.cubeisland.engine.parser.rule.token.Epsilon.EPSILON;
import static de.cubeisland.engine.parser.rule.token.automate.action.TokenActions.INTEGER;
import static de.cubeisland.engine.parser.rule.token.automate.action.TokenActions.STRING;
import static java.util.regex.Pattern.compile;

public abstract class MessageCompositorSpec
{
    public static final TokenSpec NUMBER = new ParametrizedTokenSpec<Integer>("NUMBER", compile("0|[1-9][0-9]*"), INTEGER);
    public static final TokenSpec ID = new ParametrizedTokenSpec<String>("ID", compile("[a-zA-Z]+"), STRING);
    public static final TokenSpec NAME = new ParametrizedTokenSpec<String>("NAME", compile("[a-z][a-z0-9\\- _]+"), STRING);
    public static final TokenSpec VALUE = new ParametrizedTokenSpec<String>("VALUE", compile("[^,]+"), STRING);
    public static final TokenSpec TEXT = new ParametrizedTokenSpec<String>("TEXT", compile(".+"), STRING);
    public static final TokenSpec LABEL = new ParametrizedTokenSpec<String>("LABEL", compile("[^:\\}]+"), STRING);

    public static final TokenSpec BEGIN_MACRO = new SimpleTokenSpec("{");
    public static final TokenSpec END_MACRO = new SimpleTokenSpec("}");
    public static final TokenSpec SEGMENT_SEPARATOR = new SimpleTokenSpec(":");
    public static final TokenSpec LABEL_SEPARATOR = new SimpleTokenSpec("#");
    public static final TokenSpec ARGUMENT_SEPARATOR = new SimpleTokenSpec(",");
    public static final TokenSpec VALUE_SEPARATOR = new SimpleTokenSpec("=");

    public static final Variable message = new Variable("message");
    public static final Variable text = new Variable("text");
    public static final Variable macro = new Variable("macro");
    public static final Variable rule = new Variable("rule");
    public static final Variable name = new Variable("name");
    public static final Variable args = new Variable("args");
    public static final Variable arg = new Variable("arg");
    public static final Variable named = new Variable("named");

    public static final Grammar GRAMMAR = Grammar
        .with(message.produces(message, macro, text).reactingWith(SKIP))
        .with(message.produces(text).reactingWith(SKIP))
        .with(text.produces(TEXT).reactingWith(SKIP))
        .with(text.produces(EPSILON).reactingWith(SKIP))
        .with(macro.produces(BEGIN_MACRO, rule, END_MACRO).reactingWith(SKIP))
        .with(rule.produces(NUMBER, SEGMENT_SEPARATOR, name, SEGMENT_SEPARATOR, args).reactingWith(SKIP))
        .with(rule.produces(NUMBER, SEGMENT_SEPARATOR, name).reactingWith(SKIP))
        .with(rule.produces(NUMBER).reactingWith(SKIP))
        .with(rule.produces(EPSILON).reactingWith(SKIP))
        .with(name.produces(ID, LABEL_SEPARATOR, LABEL).reactingWith(SKIP))
        .with(name.produces(ID).reactingWith(SKIP))
        .with(args.produces(arg, ARGUMENT_SEPARATOR, args).reactingWith(SKIP))
        .with(args.produces(arg).reactingWith(SKIP))
        .with(arg.produces(named).reactingWith(SKIP))
        .with(arg.produces(VALUE).reactingWith(SKIP))
        .with(named.produces(NAME, VALUE_SEPARATOR, VALUE).reactingWith(SKIP))
        .startingWith(message);
}
