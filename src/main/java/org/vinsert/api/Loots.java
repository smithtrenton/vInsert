package org.vinsert.api;

import org.vinsert.api.collection.queries.LootQuery;
import org.vinsert.api.wrappers.Loot;

import java.util.List;

/**
 * @author const_
 * @author Cov
 */
public interface Loots {

    /**
     * Gets all the ground items within the loaded region
     *
     * @return an array containing all ground items in loaded region
     */
    List<Loot> getAll();

    /**
     * Constructs a query for finding ground items
     *
     * @return query
     */
    LootQuery find();

    LootQuery find(String... names);

    LootQuery find(int... ids);
}
