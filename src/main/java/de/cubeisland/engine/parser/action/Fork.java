package de.cubeisland.engine.parser.action;

public class Fork implements Action
{
    private final Action left;
    private final Action right;

    public Fork(Action left, Action right)
    {
        this.left = left;
        this.right = right;
    }

    public Action getLeft()
    {
        return left;
    }

    public Action getRight()
    {
        return right;
    }
}
