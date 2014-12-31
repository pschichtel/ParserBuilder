package de.cubeisland.engine.parser.rule;

import java.util.Collections;
import java.util.List;
import de.cubeisland.engine.parser.Variable;

import static de.cubeisland.engine.parser.rule.Reaction.SkipReaction.SKIP;
import static de.cubeisland.engine.parser.rule.token.Epsilon.EPSILON;
import static java.util.Arrays.asList;

public class Rule
{
    private final Variable head;
    private final List<RuleElement> body;
    private final Reaction reaction;
    private final boolean epsilonProducing;

    public Rule(Variable head, List<RuleElement> body, Reaction reaction)
    {
        this.head = head;
        this.body = Collections.unmodifiableList(body);
        this.reaction = reaction;
        this.epsilonProducing = this.body.size() == 1 && this.body.get(0) == EPSILON;
    }

    public Variable getHead() {
        return this.head;
    }

    public List<RuleElement> getBody()
    {
        return body;
    }

    public Reaction getReaction()
    {
        return reaction;
    }

    public boolean isEpsilonProducing()
    {
        return this.epsilonProducing;
    }

    public MarkedRule mark()
    {
        return new MarkedRule(0);
    }

    @Override
    public String toString()
    {
        StringBuilder ruleString = new StringBuilder(head.getName()).append(" →");
        for (final RuleElement element : body)
        {
            ruleString.append(' ').append(element.getName());
        }
        return ruleString.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final Rule rule = (Rule)o;

        if (!body.equals(rule.body))
        {
            return false;
        }
        if (!head.equals(rule.head))
        {
            return false;
        }
        if (!reaction.equals(rule.reaction))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = head.hashCode();
        result = 31 * result + body.hashCode();
        result = 31 * result + reaction.hashCode();
        return result;
    }

    public class MarkedRule
    {
        private final int markPosition;

        private MarkedRule(int markPosition)
        {
            this.markPosition = markPosition;
        }

        public Rule getRule()
        {
            return Rule.this;
        }

        public RuleElement getMarkedElement()
        {
            if (isFinished())
            {
                return null;
            }
            return getBody().get(markPosition);
        }

        public MarkedRule consumeOne()
        {
            if (isFinished())
            {
                return this;
            }
            return new MarkedRule(this.markPosition + 1);
        }

        public boolean isFinished()
        {
            return this.markPosition >= getBody().size();
        }

        @Override
        public String toString()
        {
            RuleElement marked = getMarkedElement();
            StringBuilder out = new StringBuilder(head.getName()).append(" →");
            for (final RuleElement element : body)
            {
                out.append(' ');
                if (element == marked)
                {
                    out.append('•');
                }
                out.append(element.getName());
            }
            if (marked == null)
            {
                out.append('•');
            }
            return out.toString();
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            final MarkedRule that = (MarkedRule)o;

            if (markPosition != that.markPosition)
            {
                return false;
            }

            if (!getRule().equals(that.getRule()))
            {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            return getRule().hashCode();
        }
    }

    public static BodyBuilder head(Variable head)
    {
        return new BodyBuilder(head);
    }

    public static class BodyBuilder
    {
        private final Variable head;

        private BodyBuilder(Variable head)
        {
            this.head = head;
        }

        public ReactionBuilder produces(RuleElement... body)
        {
            return new ReactionBuilder(head, asList(body));
        }
    }

    public static class ReactionBuilder
    {
        private final Variable head;
        private final List<RuleElement> body;

        private ReactionBuilder(Variable head, List<RuleElement> body)
        {
            this.head = head;
            this.body = body;
        }

        public Rule skip()
        {
            return this.reactingWith(SKIP);
        }

        public Rule reactingWith(Reaction reaction)
        {
            return new Rule(head, body, reaction);
        }
    }
}