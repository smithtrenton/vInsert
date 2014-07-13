package org.vinsert.core.model;

import java.util.List;

/**
 * A property to be serialized to disk
 *
 * @author tommo
 */
public final class Property {
    private static final Container<Property> container = new Container<Property>("properties");

    private String key;

    private String value;

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Property() {

    }

    public static void defaults() {
        if (get("sources") == null && get("randoms") == null) {
            Property sources = new Property("sources", System.getProperty("user.home") + "/vInsert/Scripts/");
            Property randoms = new Property("randoms", System.getProperty("user.home") + "/vInsert/Randoms/");
            sources.save();
            randoms.save();
        }

        if (get("http_proxy") == null) {
            Property proxy = new Property("http_proxy", "");
            proxy.save();
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "[key=" + key + ", value=" + value + "]";
    }

    public static Container<Property> getContainer() {
        return container;
    }

    public void save() {
        if (container.contains(this)) {
            container.remove(this);
        }
        container.add(this);
        container.save();
    }

    public void remove() {
        container.remove(this);
        container.save();
    }

    public static Property get(String key) {
        for (Property p : getAll()) {
            if (p.getKey().equals(key)) {
                return p;
            }
        }
        return null;
    }

    public static List<Property> getAll() {
        return container.getAll();
    }

}

