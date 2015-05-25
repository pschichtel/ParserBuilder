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
package de.cubeisland.engine.parser.messagecompositor;

import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenClass;
import de.cubeisland.engine.parser.rule.token.SimpleTokenClass;
import de.cubeisland.engine.parser.rule.token.TokenClass;

import static de.cubeisland.engine.parser.rule.Reaction.SkipReaction.SKIP;
import static de.cubeisland.engine.parser.rule.token.Epsilon.EPSILON;
import static de.cubeisland.engine.parser.rule.token.automate.action.TokenActions.INTEGER;
import static de.cubeisland.engine.parser.rule.token.automate.action.TokenActions.STRING;
import static java.util.regex.Pattern.compile;

public abstract class MessageCompositorSpec
{
    public static final TokenClass NUMBER = new ParametrizedTokenClass<Integer>("NUMBER", compile("0|[1-9][0-9]*"), INTEGER);
    public static final TokenClass ID = new ParametrizedTokenClass<String>("ID", compile("[a-zA-Z]+"), STRING);
    public static final TokenClass NAME = new ParametrizedTokenClass<String>("NAME", compile("[a-z][a-z0-9\\- _]+"), STRING);
    public static final TokenClass VALUE = new ParametrizedTokenClass<String>("VALUE", compile("[^,]+"), STRING);
    public static final TokenClass TEXT = new ParametrizedTokenClass<String>("TEXT", compile(".+"), STRING);
    public static final TokenClass LABEL = new ParametrizedTokenClass<String>("LABEL", compile("[^:\\}]+"), STRING);

    public static final TokenClass BEGIN_MACRO = new SimpleTokenClass("{");
    public static final TokenClass END_MACRO = new SimpleTokenClass("}");
    public static final TokenClass SEGMENT_SEPARATOR = new SimpleTokenClass(":");
    public static final TokenClass LABEL_SEPARATOR = new SimpleTokenClass("#");
    public static final TokenClass ARGUMENT_SEPARATOR = new SimpleTokenClass(",");
    public static final TokenClass VALUE_SEPARATOR = new SimpleTokenClass("=");

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
