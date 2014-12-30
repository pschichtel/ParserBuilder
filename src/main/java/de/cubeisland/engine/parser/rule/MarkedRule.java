package de.cubeisland.engine.parser.rule;

public class MarkedRule
{
    private final int markPosition;
    private final Rule wrapped;

    public MarkedRule(Rule wrapped)
    {
        this(wrapped, 0);
    }

    private MarkedRule(Rule wrapped, int markPosition)
    {
        this.wrapped = wrapped;
        this.markPosition = markPosition;
    }

    public Rule getRule()
    {
        return this.wrapped;
    }

    public RuleElement getMarkedElement()
    {
        if (isFinished())
        {
            return null;
        }
        return this.wrapped.getElements().get(markPosition);
    }

    public MarkedRule moveMark() {
        if (isFinished())
        {
            return this;
        }
        return new MarkedRule(this.wrapped, this.markPosition + 1);
    }

    public boolean isFinished()
    {
        return this.markPosition >= this.wrapped.getElements().size();
    }
}
