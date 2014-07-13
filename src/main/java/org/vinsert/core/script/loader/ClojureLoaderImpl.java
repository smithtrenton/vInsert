package org.vinsert.core.script.loader;

import clojure.lang.*;
import com.google.common.base.Predicate;
import org.apache.log4j.Logger;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.script.stub.AgnosticStub;
import org.vinsert.core.script.stub.ClojureFileScriptStub;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Clojure source loader implementation
 *
 * @author tommo
 */
public final class ClojureLoaderImpl extends LocalScriptLoader {

    private static final Logger logger = Logger.getLogger(ClojureLoaderImpl.class);
    public static final Predicate<File> CLOJURE_FILE_PREDICATE = new Predicate<File>() {
        @Override
        public boolean apply(File file) {
            return file.getName().endsWith(".clj");
        }
    };

    public ClojureLoaderImpl() {
        super(CLOJURE_FILE_PREDICATE);
    }

    @Override
    public void loadFile(File file, List<AgnosticStub<AbstractScript, ScriptManifest>> scripts, List<AgnosticStub<RandomSolver, RandomManifest>> randoms) {
        /*
         * This var is set to the last function defined in the clojure file
         */
        Var object = (Var) RT.var("clojure.core", "load-file").invoke(file.getAbsolutePath());
        boolean loaded = false;
        Namespace ns = object.ns;
        for (Object o : ns.getMappings()) {
            MapEntry entry = (MapEntry) o;
            if (entry.getValue() instanceof Var) {
                Var var = (Var) entry.getValue();
                if (var.meta() instanceof PersistentHashMap && hasManifest(var)) {
                    try {
                        loaded = true;
                        logger.info("Loading clojure script in file: " + file.getAbsolutePath());
                        final PersistentHashMap manifestMap = (PersistentHashMap) var.meta();
                        final ScriptManifest manifest = createManifest(manifestMap);

                        ClojureFileScriptStub<AbstractScript, ScriptManifest> stub = ClojureFileScriptStub.create(var, manifest);
                        scripts.add(stub);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!loaded) {
            logger.debug("Ignored clojure file " + file.getName() +
                    ", has no defined scripts with manifests");
        }
    }

    /**
     * Checks if the var has a metadata map which a manifest is buildable from
     *
     * @param var
     * @return
     */
    private boolean hasManifest(Var var) {
        final PersistentHashMap map = (PersistentHashMap) var.meta();
        return keywordLookup(":script-name", map, null) != null
                && keywordLookup(":script-author", map, null) != null
                && keywordLookup(":script-description", map, null) != null;

    }

    private <T> Object keywordLookup(String key, PersistentHashMap map, T defaultValue) {
        for (Object o : map.keySet()) {
            Keyword k = (Keyword) o;
            if (k.toString().contains(key)) {
                return map.valAt(k);
            }
        }
        return defaultValue;
    }

    private ScriptManifest createManifest(final PersistentHashMap map) {
        return new ScriptManifest() {
            @Override
            public String name() {
                return (String) keywordLookup(":script-name", map, null);
            }

            @Override
            public String author() {
                return (String) keywordLookup(":script-author", map, null);
            }

            @Override
            public String description() {
                return (String) keywordLookup(":script-description", map, null);
            }

            @Override
            public String category() {
                return (String) keywordLookup(":script-category", map, "Other");
            }

            @Override
            public String forumLink() {
                return (String) keywordLookup(":script-forum-link", map, "None");
            }

            @Override
            public double version() {
                return (double) keywordLookup(":script-version", map, 1.0);
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ScriptManifest.class;
            }
        };
    }

}
