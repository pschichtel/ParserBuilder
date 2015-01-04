package de.cubeisland.engine.parser.factory.result;

import java.util.Collections;
import java.util.List;
import de.cubeisland.engine.parser.parser.Parser;

public class FailedResult<T extends Parser> extends CompilationResult<T>
{
    private final List<Conflict> conflicts;

    public FailedResult(List<Conflict> conflicts)
    {
        this.conflicts = Collections.unmodifiableList(conflicts);
    }

    public List<Conflict> getConflicts()
    {
        return conflicts;
    }
}
