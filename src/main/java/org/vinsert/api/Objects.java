package org.vinsert.api;

import org.vinsert.api.collection.queries.GameObjectQuery;
import org.vinsert.api.wrappers.GameObject;

import java.util.List;

/**
 * A method class for object interaction
 */
public interface Objects {

    /**
     * a method which gets all the {@link org.vinsert.api.wrappers.GameObject} in the current region
     *
     * @return an array of {@link org.vinsert.api.wrappers.GameObject} in the current region
     */
    List<GameObject> getAll();

    /**
     * Constructs a query for finding objects
     *
     * @return query
     */
    GameObjectQuery find();

    /**
     * Constructs a query for finding objects using the IDs
     *
     * @return query
     */
    GameObjectQuery find(int... ids);

    GameObjectQuery find(String... names);

}
