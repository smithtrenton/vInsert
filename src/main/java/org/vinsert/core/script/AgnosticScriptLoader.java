package org.vinsert.core.script;

import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.script.stub.AgnosticStub;

import java.util.List;

/**
 * Marks an agnostic script loader
 *
 * @author tommo
 */
public interface AgnosticScriptLoader {

    /**
     * Instructs the script loader implementation to load its scripts into the given collections
     * (dependant on implementation)
     *
     * @param scripts The general scripts collection
     * @param randoms The random solver scripts collection
     */
    void load(List<AgnosticStub<AbstractScript, ScriptManifest>> scripts, List<AgnosticStub<RandomSolver, RandomManifest>> randoms);

}
