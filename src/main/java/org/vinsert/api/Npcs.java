package org.vinsert.api;

import org.vinsert.api.collection.queries.NpcQuery;
import org.vinsert.api.wrappers.Npc;

import java.util.List;

/**
 *
 */
public interface Npcs {

    /**
     * Gets all loaded NPCs in the region.
     *
     * @return npcs
     */
    List<Npc> getAll();

    /**
     * Creates a new NPC query.
     *
     * @return query
     */
    NpcQuery find();

    /**
     * Creates an NPC query using IDs for matching.
     *
     * @param ids ids
     * @return query
     */
    NpcQuery find(int... ids);

    /**
     * Creates an NPC query using name for matching.
     *
     * @param names names
     * @return query
     */
    NpcQuery find(String... names);

}
