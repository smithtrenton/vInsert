package org.vinsert.core.script.loader;

import com.google.common.base.Predicate;
import org.apache.log4j.Logger;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.model.Source;
import org.vinsert.core.script.AgnosticScriptLoader;
import org.vinsert.core.script.stub.AgnosticStub;

import java.io.File;
import java.util.List;

/**
 * An abstract base class for a local file system script loader
 *
 * @author tommo
 */
public abstract class LocalScriptLoader implements AgnosticScriptLoader {

    private static final Logger logger = Logger.getLogger(LocalScriptLoader.class);
    private Predicate<File> filePredicate;

    public LocalScriptLoader(Predicate<File> filePredicate) {
        this.filePredicate = filePredicate;
    }

    @Override
    public final void load(List<AgnosticStub<AbstractScript, ScriptManifest>> scripts, List<AgnosticStub<RandomSolver, RandomManifest>> randoms) {
        List<Source> allSources = Source.getAll();
        for (Source source : allSources) {
            File sourceDirectory = new File(source.getSource());
            logger.info("Loading scripts from source: " + sourceDirectory.toString());
            loadDirectory(sourceDirectory, scripts, randoms);
            logger.info("Done!");
        }
    }

    /**
     * Instructs the script loading implementation to load a given file
     * and append it to one of the script collections (dependant on implementation)
     *
     * @param file    The file to load
     * @param scripts The general scripts collection
     * @param randoms The random solvers scripts collection
     */
    public abstract void loadFile(File file, List<AgnosticStub<AbstractScript, ScriptManifest>> scripts, List<AgnosticStub<RandomSolver, RandomManifest>> randoms);

    /**
     * Instructs the script loading implementation to load all scripts
     * from the given directory (and subsequent child directories) into the given script collections
     *
     * @param directory The directory to load from
     * @param scripts   The general scripts collection
     * @param randoms   The random solvers scripts collection
     */
    public final void loadDirectory(File directory, List<AgnosticStub<AbstractScript, ScriptManifest>> scripts, List<AgnosticStub<RandomSolver, RandomManifest>> randoms) {
        File[] files = directory.listFiles();
        for (File file : files != null ? files : new File[0]) {
            try {
                if (file.isDirectory()) {
                    logger.info("Going into " + file.getAbsolutePath());
                    loadDirectory(file, scripts, randoms);
                } else if (filePredicate.apply(file)) {
                    logger.info("Loading " + file.getName());
                    loadFile(file, scripts, randoms);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Failed to load source " + file.getName(), e);
            }
        }
    }

}
