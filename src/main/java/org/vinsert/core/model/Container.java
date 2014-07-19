package org.vinsert.core.model;

import com.google.gson.Gson;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.apache.log4j.Logger;
import org.vinsert.core.exception.ContainerException;
import org.vinsert.util.Configuration;

import java.io.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Base class for data containers.
 * Serializes to JSON and back to objects for storage.
 */
public class Container<T> {
    private static final JSONSerializer jsonSerializer = new JSONSerializer();
    private static final Logger logger = Logger.getLogger(Container.class);
    private List<T> elements = newArrayList();
    private final String fileName;

    public Container(String fileName) {
        this.fileName = System.getProperty("user.home") + "/vInsert/Settings/" + fileName + ".json";
    }

    public final void load() {
        try {
            File file = new File(fileName);
            logger.debug("Loading container: " + file.getAbsolutePath());
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new ContainerException("Couldn't create container file.");
                }
                save();
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            elements = new JSONDeserializer<List<T>>().deserialize(reader);
            reader.close();
            logger.debug("Loaded " + elements.size() + " entries.");
        } catch (Exception e) {
            throw new ContainerException("Couldn't open container!", e);
        }
    }

    public final void save() {
        try {
            File file = new File(fileName);
            logger.info("Loading container: " + file.getAbsolutePath());
            if (!file.exists() && !file.createNewFile()) {
                throw new ContainerException("Couldn't create container file.");
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintStream out = new PrintStream(fileOutputStream);
            out.print(jsonSerializer.deepSerialize(elements));
            out.flush();
            out.close();
            fileOutputStream.close();
            logger.info("Loaded " + elements.size() + " entries.");
        } catch (Exception e) {
            throw new ContainerException("Couldn't open container!", e);
        }
    }

    public final void add(T element) {
        elements.add(element);
    }

    public final void remove(T element) {
        elements.remove(element);
    }

    public final boolean contains(T element) {
        return elements.contains(element);
    }

    public final List<T> getAll() {
        return elements;
    }

    static {
        File file = new File(Configuration.SETTINGS_DIR + File.separator);
        if (!file.exists() && !file.mkdirs()) {
            throw new ContainerException("Failed to create settings directory for containers.");
        }
    }

    public static interface Serializer<T> {

        abstract List<T> deserialize(Gson gson, String json);

    }
}