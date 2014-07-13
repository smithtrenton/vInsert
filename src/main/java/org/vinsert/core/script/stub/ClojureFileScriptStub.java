package org.vinsert.core.script.stub;

import clojure.lang.Var;
import org.vinsert.api.script.AbstractScript;

/**
 * Clojure script stub
 *
 * @author tommo
 */
public final class ClojureFileScriptStub<T, M> implements AgnosticStub<T, M> {

    private Var var;
    private M manifest;

    private ClojureFileScriptStub(Var var, M manifest) {
        this.var = var;
        this.manifest = manifest;
    }

    /**
     * Static builder method
     * <p/>
     * Note it is simple for ease of use since the constructor requires type parameters as well as parameters
     *
     * @param var
     * @param manifest
     * @param <T>
     * @param <M>
     * @return
     */
    public static <T, M> ClojureFileScriptStub<AbstractScript, M> create(Var var, M manifest) {
        return new ClojureFileScriptStub<AbstractScript, M>(var, manifest);
    }

    @Override
    public T instantiate() throws Exception {
        return (T) var.get();
    }

    @Override
    public M manifest() {
        return manifest;
    }

}
