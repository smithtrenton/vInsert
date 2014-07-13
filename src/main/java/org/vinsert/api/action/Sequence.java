package org.vinsert.api.action;

import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.exception.ExecutionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public final class Sequence {
    private final List<Step> paths = new ArrayList<Step>();
    private final AbstractScript script;

    public Sequence(AbstractScript script, List<Step> paths) {
        this.paths.addAll(paths);
        this.script = script;
    }

    public Sequence(AbstractScript script, Step... paths) {
        this(script, Arrays.asList(paths));
    }

    public Sequence(AbstractScript script) {
        this.script = script;
    }

    public void addPath(Step step) {
        this.paths.add(step);
    }

    public void removePath(Step step) {
        this.paths.remove(step);
    }

    public void execute() throws ExecutionException {
        try {
            for (Step path : paths) {
                Executor executor = Executor.from(this, path);
                if (executor == null) {
                    continue;
                }
                executor.execute();
            }
        } catch (Exception e) {
            throw new ExecutionException("Execution of sequence failed", e);
        }
    }

    public List<Step> getPaths() {
        return paths;
    }

    public AbstractScript getScript() {
        return script;
    }
}
