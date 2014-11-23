package de.cubeisland.engine.parser;

import java.util.ArrayList;
import java.util.List;

public class Grammar
{
    private final List<Rule> rules;

    private Grammar()
    {
        this(new ArrayList<Rule>());
    }

    protected Grammar(List<Rule> rules)
    {
        this.rules = rules;
    }

    public static final class Builder
    {
        private final Grammar grammar;

        public Builder() {
            this.grammar = new Grammar();
        }

        public Grammar get() {
            return this.grammar;
        }
    }
}
