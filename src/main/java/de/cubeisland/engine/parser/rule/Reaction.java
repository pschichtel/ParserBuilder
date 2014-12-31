package de.cubeisland.engine.parser.rule;

public interface Reaction
{
    public void react();

    public static final class SkipReaction implements Reaction
    {
        public static final SkipReaction SKIP = new SkipReaction();

        public void react()
        {}
    }
}
