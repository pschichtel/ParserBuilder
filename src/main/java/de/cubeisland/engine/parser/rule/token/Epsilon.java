package de.cubeisland.engine.parser.rule.token;

import de.cubeisland.engine.parser.rule.RuleElement;

public class Epsilon extends RuleElement
{
    public static final Epsilon EPSILON = new Epsilon();

    public Epsilon()
    {
        super("ε");
    }
}
