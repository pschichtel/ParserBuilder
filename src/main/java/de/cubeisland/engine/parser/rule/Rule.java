package de.cubeisland.engine.parser.rule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import de.cubeisland.engine.parser.Variable;

public class Rule
{
    private final Variable head;
    private final List<RuleElement> elements;

    public Rule(Variable head, RuleElement... body)
    {
        this(head, Arrays.asList(body));
    }

    public Rule(Variable head, List<RuleElement> body)
    {
        this.head = head;
        this.elements = Collections.unmodifiableList(body);
    }

    public Variable getHead() {
        return this.head;
    }

    public List<RuleElement> getElements()
    {
        return elements;
    }

    public MarkedRule mark()
    {
        return new MarkedRule(0);
    }

    @Override
    public String toString()
    {
        StringBuilder ruleString = new StringBuilder(head.getName()).append(" ->");
        for (final RuleElement element : elements)
        {
            ruleString.append(' ').append(element.getName());
        }
        return ruleString.toString();
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
            return getElements().get(markPosition);
        }

        public MarkedRule moveMark()
        {
            if (isFinished())
            {
                return this;
            }
            return new MarkedRule(this.markPosition + 1);
        }

        public boolean isFinished()
        {
            return this.markPosition >= getElements().size();
        }
    }
}
