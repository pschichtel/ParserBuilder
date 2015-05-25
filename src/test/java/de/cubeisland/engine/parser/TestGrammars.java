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
import de.cubeisland.engine.parser.rule.token.ParametrizedToken;
import de.cubeisland.engine.parser.rule.token.ParametrizedTokenClass;
import de.cubeisland.engine.parser.rule.token.Token;
import de.cubeisland.engine.parser.rule.token.automate.action.TokenAction;
import de.cubeisland.engine.parser.rule.token.TokenClass;

import static de.cubeisland.engine.parser.TestGrammars.SimpleExpr.*;
import static de.cubeisland.engine.parser.TestGrammars.FirstFollowExpr.*;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.parametrized;
import static de.cubeisland.engine.parser.rule.token.TokenSpecFactory.simple;

public class TestGrammars
{

    public static class SimpleExpr
    {
        public static final Variable product = new Variable("product");
        public static final Variable factor = new Variable("factor");
        public static final Variable expr = new Variable("expr");

        public static final TokenClass ADD = simple("+");
        public static final TokenClass SUB = simple("-");
        public static final TokenClass MUL = simple("*");
        public static final TokenClass DIV = simple("/");
        public static final TokenClass BGN = simple("(");
        public static final TokenClass END = simple(")");

        public static final ParametrizedTokenClass<Integer> NUM = parametrized("NUM", "0|[1-9][0-9]*", new TokenAction() {
            @SuppressWarnings("unchecked")
            @Override
            public Token act(TokenClass tokenClass, String lexeme)
            {
                return new ParametrizedToken<Integer>((ParametrizedTokenClass<Integer>)tokenClass, Integer.valueOf(lexeme));
            }
        });
    }

    public static class FirstFollowExpr
    {
        public static final Variable s = new Variable("s");
        public static final Variable a = new Variable("a");
        public static final Variable c = new Variable("c");
        public static final Variable d = new Variable("d");

        public static final TokenClass B = simple("b");
        public static final TokenClass C = simple("c");
        public static final TokenClass D = simple("d");
        public static final TokenClass L = simple("l");
        public static final TokenClass M = simple("m");
        public static final TokenClass H = simple("h");
    }

    public static final Grammar SIMPLE_EXPR = Grammar
        .with(expr.produces(expr, ADD, product).skip())
        .with(expr.produces(expr, SUB, product).skip())
        .with(expr.produces(product).skip())
        .with(product.produces(product, MUL, factor).skip())
        .with(product.produces(product, DIV, factor).skip())
        .with(product.produces(factor).skip())
        .with(factor.produces(BGN, expr, END).skip())
        .with(factor.produces(NUM).skip())
        .startingWith(expr);

    public static final Grammar SIMPLE_EXPR_WITH_EPS = Grammar
        .with(expr.produces(expr, ADD, product).skip())
        .with(expr.produces(expr, SUB, product).skip())
        .with(expr.produces(product).skip())
        .with(expr.produces(Epsilon.EPSILON).skip())
        .with(product.produces(product, MUL, factor).skip())
        .with(product.produces(product, DIV, factor).skip())
        .with(product.produces(factor).skip())
        .with(factor.produces(BGN, expr, END).skip())
        .with(factor.produces(NUM).skip())
        .startingWith(expr);

    public static final Grammar FIRST_N_FOLLOW_TEST_GRAMMAR = Grammar
            .with(s.produces(a, B, c).skip())
            .with(a.produces(C, D).skip())
            .with(c.produces(L, M, d, L).skip())
            .with(d.produces(H).skip())
            .startingWith(s);
}
