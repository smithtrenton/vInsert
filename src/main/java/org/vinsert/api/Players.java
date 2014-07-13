package org.vinsert.api;

import org.vinsert.api.collection.queries.PlayerQuery;
import org.vinsert.api.wrappers.Player;

import java.util.List;

/**
 * Methods for dealing with Players in the game.
 */
public interface Players {

    /**
     * Gets the currently logged in player
     *
     * @return local player
     */
    Player getLocal();

    /**
     * Gets all players within the current region.
     *
     * @return players
     */
    List<Player> getAll();

    /**
     * Gets a new PlayerQuery which can be used for
     * finding and selecting players matching certain
     * conditions.
     *
     * @return query
     */
    PlayerQuery find();

    /**
     * Creates a query using a Player's most basic attribute, being it's name.
     *
     * @return query
     */
    PlayerQuery find(String... names);

}
