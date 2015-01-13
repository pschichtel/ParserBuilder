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
package de.cubeisland.engine.parser;

import de.cubeisland.engine.parser.grammar.Grammar;
import de.cubeisland.engine.parser.rule.token.Epsilon;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenSpec;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

import static de.cubeisland.engine.parser.TestGrammars.SimpleExpr.*;
import static de.cubeisland.engine.parser.rule.Rule.head;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.parametrized;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.simple;

public class TestGrammars
{

    public static class SimpleExpr
    {
        public static final Variable product = new Variable("product");
        public static final Variable factor = new Variable("factor");
        public static final Variable expr = new Variable("expr");

        public static final TokenSpec ADD = simple("ADD", "+");
        public static final TokenSpec SUB = simple("SUB", "-");
        public static final TokenSpec MUL = simple("MUL", "*");
        public static final TokenSpec DIV = simple("DIV", "/");
        public static final TokenSpec BGN = simple("BGN", "(");
        public static final TokenSpec END = simple("END", ")");

        public static final ParametrizedTokenSpec<Integer> NUM = parametrized("NUM", "0|[1-9][0-9]*", Integer.class);
    }

    public static final Grammar SIMPLE_EXPR = Grammar
        .with(head(expr).produces(expr, ADD, product).skip())
        .with(head(expr).produces(expr, SUB, product).skip())
        .with(head(expr).produces(product).skip())
        .with(head(product).produces(product, MUL, factor).skip())
        .with(head(product).produces(product, DIV, factor).skip())
        .with(head(product).produces(factor).skip())
        .with(head(factor).produces(BGN, expr, END).skip())
        .with(head(factor).produces(NUM).skip())
        .startingWith(expr);

    public static final Grammar SIMPLE_EXPR_WITH_EPS = Grammar
        .with(head(expr).produces(expr, ADD, product).skip())
        .with(head(expr).produces(expr, SUB, product).skip())
        .with(head(expr).produces(product).skip())
        .with(head(expr).produces(Epsilon.EPSILON).skip())
        .with(head(product).produces(product, MUL, factor).skip())
        .with(head(product).produces(product, DIV, factor).skip())
        .with(head(product).produces(factor).skip())
        .with(head(factor).produces(BGN, expr, END).skip())
        .with(head(factor).produces(NUM).skip())
        .startingWith(expr);
}
