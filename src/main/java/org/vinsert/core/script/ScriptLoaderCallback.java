package org.vinsert.core.script;

import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.script.stub.AgnosticStub;

import java.util.List;

/**
 * Callbacks are the shit nowadays.
 */
public interface ScriptLoaderCallback {

    void onCompletion(List<AgnosticStub<AbstractScript, ScriptManifest>> results);

}
