package de.cubeisland.engine.parser.util;

public class OrderedPair<L, R> implements Pair<L, R>
{
    private final L left;
    private final R right;

    public OrderedPair(L left, R right)
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof OrderedPair))
        {
            return false;
        }

        OrderedPair that = (OrderedPair) o;

        if (left != null ? !left.equals(that.left) : that.left != null)
        {
            return false;
        }
        if (right != null ? !right.equals(that.right) : that.right != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}
