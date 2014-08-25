package org.vinsert.gui;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for managing controller instances.
 *
 * @author : const_
 */
public final class ControllerManager {
    private static final Map<Class<?>, Controller> CONTROLLER_MAP = new HashMap<>();

    private ControllerManager() {

    }

    public static <T extends Controller> T get(Class<T> clazz) {
        return (T) CONTROLLER_MAP.get(clazz);
    }

    public static void add(Class<?> clazz, Controller controller) {
        CONTROLLER_MAP.put(clazz, controller);
    }

}
