package org.vinsert.core.script.loader;

import com.google.common.base.Predicate;
import org.apache.log4j.Logger;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.script.stub.AgnosticStub;
import org.vinsert.core.script.stub.ClassFileScriptStub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Script loader implementation for class files
 *
 * @author tommo
 */
public final class ClassLoaderImpl extends LocalScriptLoader {

    private static final Logger logger = Logger.getLogger(ClassLoaderImpl.class);
    public static final Predicate<File> CLASS_FILE_PREDICATE = new Predicate<File>() {
        @Override
        public boolean apply(File file) {
            return file.getName().contains(".class") || file.getName().contains(".jar");
        }
    };

    private List<AgnosticStub<AbstractScript, ScriptManifest>> scripts;
    private List<AgnosticStub<RandomSolver, RandomManifest>> randoms;

    public ClassLoaderImpl() {
        super(CLASS_FILE_PREDICATE);
    }

    @Override
    public void loadFile(File file, List<AgnosticStub<AbstractScript, ScriptManifest>> scripts, List<AgnosticStub<RandomSolver, RandomManifest>> randoms) {
        this.scripts = scripts;
        this.randoms = randoms;
        try {
            if (file.getName().endsWith(".class")) {
                loadClassFile(file);
            } else if (file.getName().endsWith(".jar")) {
                loadJarFile(file);
            } else {
                logger.error("Unsupported file format: " + file.getName());
            }
        } catch (Exception e) {
            logger.error("Unable to load class file: " + file.toString(), e);
            e.printStackTrace();
        }
    }

    /**
     * Loads a class file
     *
     * @param rawFile The class file
     * @throws Exception
     */
    private void loadClassFile(File rawFile) throws Exception {
        String operatingSystem = System.getProperty("os.name");
        String name;
        URL[] classpathURLs = new URL[1];

        /*
         * Write once, Run nowhere
         */
        if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0 || operatingSystem.indexOf("aix") > 0) {
            name = formatClassName(rawFile.getName());
            classpathURLs[0] = new URL(rawFile.toURI().toURL().toString().replace(name, "").replace(".class", ""));
        } else {
            File temp = File.createTempFile("wut", "lefux");
            temp.deleteOnExit();
            copyFile(rawFile, temp);
            name = formatClassName(temp.getName());
            classpathURLs[0] = rawFile.toURI().toURL();
        }

        ClassLoader loader = new URLClassLoader(classpathURLs,
                Thread.currentThread().getContextClassLoader());
        String inferredPackageName = rawFile.getAbsolutePath();
        loadClass(loader.loadClass(inferredPackageName));
    }

    /**
     * Opens a jar file, iterating through all entries.
     * If a .class entry is found, finds out if it is
     * a scriptable entity, and if so, loads it.
     *
     * @param rawFile a JAR file.
     * @throws Exception
     */
    private void loadJarFile(File rawFile) throws Exception {
        File tempFile = File.createTempFile("wut", "lefux");
        tempFile.deleteOnExit();
        copyFile(rawFile, tempFile);
        JarFile file = new JarFile(tempFile, false, JarFile.OPEN_READ);
        URL[] classpathURLs = new URL[]{tempFile.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(classpathURLs,
                Thread.currentThread().getContextClassLoader());

        Enumeration<JarEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            JarEntry current = entries.nextElement();
            String name = current.getName();

            if (name.endsWith(".class")) {
                String strip = formatClassName(name);
                try {
                    loadClass(loader.loadClass(strip));
                } catch (Exception e) {
                    logger.error("Failed to load " + strip, e);
                }
            }
        }
        file.close();
    }

    @SuppressWarnings("unchecked")
    private void loadClass(Class<?> clazz) throws Exception {
        if (!hasAnnotation(clazz)) {
            logger.debug("Class " + clazz.getSimpleName() +
                    " has no annotation and was ignored.");
        } else if (AbstractScript.class.isAssignableFrom(clazz)) {
            scripts.add(ClassFileScriptStub.create((Class<? extends AbstractScript>) clazz, ScriptManifest.class));
        } else if (RandomSolver.class.isAssignableFrom(clazz)) {
            randoms.add(ClassFileScriptStub.create((Class<? extends RandomSolver>) clazz, RandomManifest.class));
        }
    }

    private boolean hasAnnotation(Class<?> clazz) {
        return clazz.getAnnotation(ScriptManifest.class) != null
                || clazz.getAnnotation(RandomManifest.class) != null;
    }

    private String formatClassName(String name) {
        return name.replaceAll("/", "\\.").replace(".class", "");
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

}
