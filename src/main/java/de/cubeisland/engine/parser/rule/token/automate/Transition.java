package de.cubeisland.engine.parser.rule.token.automate;

public interface Transition
{
    State getOrigin();
    State getDestination();
}
