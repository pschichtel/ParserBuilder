package de.cubeisland.engine.parser.type.result;

import java.util.Collections;
import java.util.List;

public class FailedResult extends CompilationResult
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
