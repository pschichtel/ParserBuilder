package de.cubeisland.engine.parser.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.cubeisland.engine.parser.rule.token.SimpleTokenSpec;
import de.cubeisland.engine.parser.rule.token.TokenSpec;
import junit.framework.TestCase;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;


public class TokenConcatterTest extends TestCase
{
    private List<TokenSpec> v = new ArrayList<TokenSpec>();
    private List<TokenSpec> w = new ArrayList<TokenSpec>();

    private SimpleTokenSpec ta = new SimpleTokenSpec("a", "a");
    private SimpleTokenSpec tb = new SimpleTokenSpec("b", "b");
    private SimpleTokenSpec tc = new SimpleTokenSpec("c", "c");

    private SimpleTokenSpec t1 = new SimpleTokenSpec("1", "1");
    private SimpleTokenSpec t2 = new SimpleTokenSpec("2", "2");
    private SimpleTokenSpec t3 = new SimpleTokenSpec("3", "3");

    private Set<List<TokenSpec>> m = new HashSet<List<TokenSpec>>();
    private Set<List<TokenSpec>> n = new HashSet<List<TokenSpec>>();
    private Set<List<TokenSpec>> o = new HashSet<List<TokenSpec>>();

    public TokenConcatterTest()
    {
        this.v.add(this.ta);
        this.v.add(this.tb);
        this.v.add(this.tc);

        this.w.add(this.t1);
        this.w.add(this.t2);
        this.w.add(this.t3);

        this.m.add(this.v);
        this.m.add(this.w);

        this.n.add(this.v);
        this.n.add(this.w);

        this.o.add(this.v);
    }

    public void testConcatPrefix() throws Exception
    {
        List<TokenSpec> solution = new ArrayList<TokenSpec>();

        solution.add(this.ta);
        solution.add(this.tb);
        solution.add(this.tc);

        solution.add(this.t1);
        solution.add(this.t2);
        solution.add(this.t3);

        for (int i = 0; i < 10; i++)
        {
            List<TokenSpec> concatList = TokenConcatter.concatPrefix(i, this.v, this.w);

            assertThat("Concatenation of two lists with k = " + i + " failed.", concatList, is(solution.subList(0, i < solution.size() ? i : solution.size())));
        }
    }

    public void testConcatPrefix1() throws Exception
    {
        for (int i = 0; i < 10; i++)
        {
            Set<List<TokenSpec>> solution = new HashSet<List<TokenSpec>>();

            solution.add(TokenConcatter.concatPrefix(i, this.v, this.w));
            solution.add(TokenConcatter.concatPrefix(i, this.v, this.v));
            solution.add(TokenConcatter.concatPrefix(i, this.w, this.v));
            solution.add(TokenConcatter.concatPrefix(i, this.w, this.w));

            Set<List<TokenSpec>> proposal = TokenConcatter.concatPrefix(i, this.m, this.n);

            assertThat("Concatenation of two sets with k = " + i + " failed.", proposal, is(solution));
        }
    }

    public void testConcatPrefix2() throws Exception
    {
        for (int i = 0; i < 10; i++)
        {
            Set<List<TokenSpec>> solution = TokenConcatter.concatPrefix(i, TokenConcatter.concatPrefix(i, this.m,
                                                                                                       this.n), this.o);

            Set<List<TokenSpec>> proposal = TokenConcatter.concatPrefix(i, this.m, this.n, this.o);

            assertThat("Concatenation of three sets with k = " + i + " failed.", proposal, is(solution));
        }
    }
}