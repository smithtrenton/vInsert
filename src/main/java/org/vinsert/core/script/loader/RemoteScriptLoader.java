package org.vinsert.core.script.loader;

import org.apache.log4j.Logger;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.script.AgnosticScriptLoader;
import org.vinsert.core.script.stub.AgnosticStub;

import java.util.List;

/**
 * Abstract base class for loading scripts from a remote location
 *
 * @author tommo
 */
public final class RemoteScriptLoader implements AgnosticScriptLoader {
    private static final Logger logger = Logger.getLogger(RemoteScriptLoader.class);

    @Override
    public void load(List<AgnosticStub<AbstractScript, ScriptManifest>> scripts, List<AgnosticStub<RandomSolver, RandomManifest>> randoms) {
        // TODO: New SDN needs to be written before we can actually implement this.
    }

}
