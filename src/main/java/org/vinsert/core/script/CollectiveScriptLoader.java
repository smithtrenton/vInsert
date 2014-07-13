package org.vinsert.core.script;

import com.google.common.collect.ImmutableSet;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.script.loader.ClassLoaderImpl;
import org.vinsert.core.script.loader.ClojureLoaderImpl;
import org.vinsert.core.script.loader.LocalScriptLoader;
import org.vinsert.core.script.stub.AgnosticStub;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Collectively loads all available scripts
 *
 * @author tommo
 */
public final class CollectiveScriptLoader {
    private static final CollectiveScriptLoader instance = new CollectiveScriptLoader();
    private final Set<LocalScriptLoader> localLoaders = new ImmutableSet.Builder<LocalScriptLoader>()
            .add(new ClassLoaderImpl(), new ClojureLoaderImpl()).build();
    private final List<AgnosticStub<AbstractScript, ScriptManifest>> cachedScripts = new ArrayList<AgnosticStub<AbstractScript, ScriptManifest>>();
    private final List<AgnosticStub<RandomSolver, RandomManifest>> cachedRandoms = new ArrayList<AgnosticStub<RandomSolver, RandomManifest>>();

    private CollectiveScriptLoader() {

    }

    /**
     * Clears any existing cached script stubs and loads them again.
     */
    public void load() {
        try {
            clearCached();
            loadBuiltin();
            for (LocalScriptLoader loader : localLoaders) {
                loader.load(cachedScripts, cachedRandoms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRandoms() {
        clearCached();
        loadBuiltin();
        for (LocalScriptLoader loader : localLoaders) {
            loader.load(cachedScripts, cachedRandoms);
        }
    }

    /**
     * Clears any existing cached script stubs and loads them again.
     */
    public void load(final ScriptLoaderCallback callback) {
        load();
        callback.onCompletion(cachedScripts);
    }

    private void loadBuiltin() {
        /*cachedScripts.add(ClassFileScriptStub.<AbstractScript, ScriptManifest>
                create(WidgetExplorerScript.class, ScriptManifest.class));*/
    }

    /**
     * Clears all cached script stubs
     */
    public void clearCached() {
        cachedScripts.clear();
        cachedRandoms.clear();
    }

    /**
     * Returns a list of loaded script stubs
     *
     * @return The list of script stubs
     */
    public List<AgnosticStub<AbstractScript, ScriptManifest>> getLoadedScripts() {
        return cachedScripts;
    }

    /**
     * Returns a list of loaded random script stubs
     *
     * @return The list of random stubs
     */
    public List<AgnosticStub<RandomSolver, RandomManifest>> getLoadedRandoms() {
        return cachedRandoms;
    }

    public static CollectiveScriptLoader getInstance() {
        return instance;
    }

}
