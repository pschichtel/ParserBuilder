package de.cubeisland.engine.parser.token;

import java.util.regex.Pattern;

public class ParametrizedTokenSpec<T> extends TokenSpec
{
    private final Class<T> valueType;

    public ParametrizedTokenSpec(String name, Pattern pattern, Class<T> valueType)
    {
        super(name, pattern);
        this.valueType = valueType;
    }

    public Class<T> getValueType()
    {
        return valueType;
    }
}
