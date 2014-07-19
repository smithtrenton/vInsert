package org.vinsert.core;

import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.vinsert.game.exception.ClassPreloadException;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ClassLoader used to load un-modified classes to bypass potential client detections.
 * Currently disabled as it causes gameplay issues
 * @author : const_
 */
public final class StaticClassLoader extends ClassLoader {
    private static final Logger logger = Logger.getLogger(StaticClassLoader.class);
    private static StaticClassLoader instance = new StaticClassLoader();
    private final Map<String, byte[]> classes = new HashMap<>();

    public void init(JarFile jarFile) {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry current = entries.nextElement();
            if (!current.getName().endsWith(".class")) {
                continue;
            }
            try {
                ClassNode node = new ClassNode();
                ClassReader reader = new ClassReader(jarFile.getInputStream(current));
                reader.accept(node, ClassReader.EXPAND_FRAMES);
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                node.accept(writer);
                classes.put(node.name, writer.toByteArray());
            } catch (IOException | ClassFormatError e) {
                throw new ClassPreloadException(current.getName(), e);
            }
        }
    }

    @Override
    public Class<?> loadClass(String name) {
        try {
            String className = formatClassName(name);
            Class<?> result;
            if ((result = defineSelf(name, classes.get(className))) != null) {
                return result;
            } else if ((result = findLoadedClass(className)) != null) {
                return result;
            } else if ((result = extractPrimitive(className)) != null) {
                return result;
            }
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("Static class not covered by ClassLoader", e);
            return null;
        }
    }

    private Class<?> defineSelf(String name, byte[] data) {
        if (data != null && data.length > 0) {
            return defineClass(name, data, 0, data.length);
        }
        return null;
    }

    private Class<?> extractPrimitive(String className) {
        switch (className) {
            case "B":
                return Byte.TYPE;
            case "I":
                return Integer.TYPE;
            case "S":
                return Short.TYPE;
            case "J":
                return Long.TYPE;
            case "Z":
                return Boolean.TYPE;
            case "F":
                return Float.TYPE;
            case "D":
                return Double.TYPE;
            case "C":
                return Character.TYPE;
            default:
                return null;
        }
    }

    private String formatClassName(String className) {
        String name = className.replaceAll("\\[", "");
        if (name.startsWith("L") && name.endsWith(";")) {
            name = name.replaceAll("L", "").replaceAll(";", "");
        }
        return name;
    }

    public static Class<?> load(String name) {
        return instance.loadClass(name);
    }

    public static void prepare() {
        try {
            if (instance.classes.isEmpty()) {
                logger.info("Loading static classes.");
                final URL url = new URL("jar:http://oldschool36.runescape.com/gamepack.jar!/");
                final JarURLConnection connection = (JarURLConnection) url.openConnection();
                instance.init(connection.getJarFile());
                logger.info("Loading completed.");
            } else {
                logger.info("Re-using static class set.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception while loading static classes.", e);
        }
    }
}
