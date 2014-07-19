package org.vinsert.core.model;

import java.util.List;

/**
 * Represents a directory for loading
 * jar files with script classes from.
 *
 * @author tobiewarburton
 */
public final class Source {
    private static final Container<Source> container = new Container<Source>("sources");

    private String source;
    private String type;
    private boolean purgeMarker;

    public Source(String source, String type) {
        this.source = source;
        this.type = type;
    }

    public Source() {
    }

    public static void defaults() {
        if (container.getAll().size() == 0) {
            Source scripts = new Source(Property.get("sources").getValue(), "");
            scripts.save();
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return source;
    }

    public static Container<Source> getContainer() {
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

    public static List<Source> getAll() {
        return container.getAll();
    }

    public boolean isPurgeMarker() {
        return purgeMarker;
    }

    public void setPurgeMarker(boolean purgeMarker) {
        this.purgeMarker = purgeMarker;
    }

}

