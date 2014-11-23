package de.cubeisland.engine.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Rule
{
    private final Variable head;
    private final List<RuleElement> elements;

    public Rule(Variable head, RuleElement... elements)
    {
        this(head, Arrays.asList(elements));
    }

    public Rule(Variable head, List<RuleElement> elements)
    {
        this.head = head;
        this.elements = Collections.unmodifiableList(elements);
    }

    public Variable getHead() {
        return this.head;
    }

    public List<RuleElement> getElements()
    {
        return elements;
    }
}
