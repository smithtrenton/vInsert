package org.vinsert.api.collection;

/**
 * A filter interface for generic types
 */
public interface Filter<T> {

    /**
     * Filter function
     *
     * @param acceptable an item that can be filtered
     * @return true if item should be retained, false otherwise.
     */
    boolean accept(T acceptable);

}
