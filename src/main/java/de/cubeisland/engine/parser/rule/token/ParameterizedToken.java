package de.cubeisland.engine.parser.rule.token;

public class ParameterizedToken<T> implements Token
{
    private final ParametrizedTokenSpec<T> spec;
    private final T value;

    public ParameterizedToken(ParametrizedTokenSpec<T> spec, T value)
    {
        this.spec = spec;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return spec.getName() + "(" + value + ')';
    }
}
