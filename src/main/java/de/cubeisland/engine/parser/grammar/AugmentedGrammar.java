package de.cubeisland.engine.parser.grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.token.EndOfFileToken;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

public class AugmentedGrammar extends BaseGrammar
{
    public static final Variable AUGMENTED_START = new AugmentedStartVariable();

    public AugmentedGrammar(Set<Variable> variables, Set<TokenSpec> tokens, List<Rule> rules, Variable start)
    {
        super(augment(variables), tokens, augment(rules, start), AUGMENTED_START);
    }

    public Rule getStartRule()
    {
        return getRules().get(0);
    }

    private static Set<Variable> augment(Set<Variable> original)
    {
        Set<Variable> augmented = new HashSet<Variable>(original);
        augmented.add(AUGMENTED_START);
        return augmented;
    }

    private static List<Rule> augment(List<Rule> original, Variable originalStart)
    {
        List<Rule> rules = new ArrayList<Rule>(original);
        rules.add(0, new Rule(AUGMENTED_START, originalStart, EndOfFileToken.EOF));
        return rules;
    }

    public static final class AugmentedStartVariable extends Variable
    {
        public AugmentedStartVariable()
        {
            super("<Start>");
        }
    }
}
