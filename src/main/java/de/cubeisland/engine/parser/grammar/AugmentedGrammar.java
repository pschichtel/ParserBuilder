package de.cubeisland.engine.parser.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.cubeisland.engine.parser.Variable;
import de.cubeisland.engine.parser.rule.Rule;
import de.cubeisland.engine.parser.rule.token.TokenSpec;

import static de.cubeisland.engine.parser.rule.Reaction.SkipReaction.SKIP;
import static de.cubeisland.engine.parser.rule.token.EndOfFileToken.EOF;
import static java.util.Arrays.asList;

public class AugmentedGrammar extends BaseGrammar
{
    public static final Variable AUGMENTED_START = new AugmentedStartVariable();

    public AugmentedGrammar(Set<Variable> variables, Set<TokenSpec> tokens, List<Rule> rules, Variable start)
    {
        super(augment(variables), augmentTokens(tokens), augment(rules, start), AUGMENTED_START);
    }

    public Rule getStartRule()
    {
        return getRules().get(0);
    }

    public Map<Variable, Set<List<TokenSpec>>> follow()
    {
        return follow(1);
    }

    public Map<Variable, Set<List<TokenSpec>>> follow(int k)
    {
        final Map<Variable, Set<List<TokenSpec>>> follow = new HashMap<Variable, Set<List<TokenSpec>>>();
        final Map<Variable, Set<List<TokenSpec>>> first = first(k);



        return follow;
    }

    private static Set<Variable> augment(Set<Variable> original)
    {
        Set<Variable> augmented = new HashSet<Variable>(original);
        augmented.add(AUGMENTED_START);
        return augmented;
    }

    private static Set<TokenSpec> augmentTokens(Set<TokenSpec> original)
    {
        Set<TokenSpec> augmented = new HashSet<TokenSpec>(original);
        augmented.add(EOF);
        return augmented;
    }

    private static List<Rule> augment(List<Rule> original, Variable originalStart)
    {
        List<Rule> rules = new ArrayList<Rule>(original);
        rules.add(0, new Rule(AUGMENTED_START, asList(originalStart, EOF), SKIP));
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
