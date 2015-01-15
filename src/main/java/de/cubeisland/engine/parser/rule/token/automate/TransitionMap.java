package de.cubeisland.engine.parser.rule.token.automate;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

final class TransitionMap
{
    private final Map<Character, Set<ExpectedTransition>> expectedTransitions;
    private final Set<SpontaneousTransition> spontaneousTransitions;
    private final Set<Character> expectedChars;

    public TransitionMap(Map<Character, Set<ExpectedTransition>> expectedTransitions, Set<SpontaneousTransition> spontaneousTransitions, Set<Character> expectedChars)
    {
        this.expectedTransitions = unmodifiableMap(expectedTransitions);
        this.spontaneousTransitions = unmodifiableSet(spontaneousTransitions);
        this.expectedChars = unmodifiableSet(expectedChars);
    }

    public Set<ExpectedTransition> getTransitionsFor(char c)
    {
        Set<ExpectedTransition> transitions = this.expectedTransitions.get(c);
        if (transitions == null)
        {
            return Collections.emptySet();
        }
        return transitions;
    }

    public Set<SpontaneousTransition> getSpontaneousTransitions()
    {
        return this.spontaneousTransitions;
    }

    public Set<Character> getExpectedChars()
    {
        return this.expectedChars;
    }
}
