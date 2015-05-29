package de.cubeisland.engine.parser.rule.token.automate.eval;

import java.util.HashSet;
import java.util.Set;
import de.cubeisland.engine.parser.rule.token.automate.DFA;
import de.cubeisland.engine.parser.rule.token.automate.FiniteAutomate;
import de.cubeisland.engine.parser.rule.token.automate.NFA;
import de.cubeisland.engine.parser.rule.token.automate.transition.Transition;

public class Evaluator
{
    public static StateMachineEvaluator eval(FiniteAutomate<? extends Transition>... automates)
    {
        if (automates.length == 0)
        {
            throw new IllegalArgumentException("No automate given!");
        }
        if (automates.length == 1)
        {
            return evaluatorFor(automates[0]);
        }
        final Set<StateMachineEvaluator> evaluators = new HashSet<StateMachineEvaluator>();
        for (final FiniteAutomate<? extends Transition> automate : automates)
        {
            evaluators.add(evaluatorFor(automate));
        }
        return new MultiEvaluator(evaluators);
    }

    private static StateMachineEvaluator evaluatorFor(FiniteAutomate<? extends Transition> automate)
    {
        if (automate instanceof DFA)
        {
            return new DFAEvaluator((DFA)automate);
        }
        if (automate instanceof NFA)
        {
            return new NFAEvaluator((NFA)automate);
        }
        throw new IllegalArgumentException("Unknown automate type: " + automate.getClass());
    }
}
