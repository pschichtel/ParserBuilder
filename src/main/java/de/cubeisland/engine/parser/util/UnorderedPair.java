package de.cubeisland.engine.parser.util;

import de.cubeisland.engine.parser.rule.token.automate.State;

public class UnorderedPair<L, R> implements Pair<L, R>
{
    private final L left;
    private final R right;

    public UnorderedPair(L left, R right)
    {
        this.left = left;
        this.right = right;
    }

    @Override
    public L getLeft()
    {
        return left;
    }

    @Override
    public R getRight()
    {
        return right;
    }

    /**
     * This implementation ignores the order of the fields, so <a, b> = <b, a>
     *
     * @param o the other object
     * @return true of they're the same
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof UnorderedPair))
        {
            return false;
        }

        UnorderedPair that = (UnorderedPair) o;

        if (left.equals(that.left) && right.equals(that.right))
        {
            return true;
        }
        if (left.equals(that.right) && right.equals(that.left))
        {
            return true;
        }
        return false;
    }

    /**
     * This implementation just adds up both hashCodes to drop ordering
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return left.hashCode() + right.hashCode();
    }

    @Override
    public String toString()
    {
        return "<" + this.left + ", " + this.right + ">";
    }
}
