package org.vinsert.api;

import org.vinsert.api.wrappers.Tile;
import org.vinsert.api.wrappers.interaction.SceneNode;

/**
 * Date: 27/08/13
 * Time: 12:43
 *
 * @author Matt Collinge
 */
public interface Walking {

    public enum Direction {
        FORWARDS, BACKWARDS
    }

    /**
     * Walks the path from one end to the other in the specified direction.
     * <p/>
     * This is best used when you want to walk a specific path without
     * using the pathfinding abilities of the api.
     * <p/>
     * The walk method uses this once it has found the path.
     *
     * @param path      path to walk.
     * @param direction direction in which to walk the path.
     */
    void traverse(Tile[] path, Direction direction);

    /**
     * Finds and walks a path across the whole of runescape to the destination.
     * This uses the SDN to find a path across the entirety of the runescape
     * map.
     * <p/>
     * This currently only supports the surface, although it does work in
     * dungeons, it just doesn't yet traverse between the two.
     *
     * @param destination target SceneNode anywhere in runescape.
     */
    void walk(SceneNode destination);

    /**
     * Finds and walks a path across the whole of runescape to the destination.
     * This uses the SDN to find a path across the entirety of the runescape
     * map.
     * <p/>
     * This currently only supports the surface, although it does work in
     * dungeons, it just doesn't yet traverse between the two.
     *
     * @param tile target Tile anywhere in runescape.
     */
    void walk(Tile tile);

    /**
     * @param x X value of the destination tile.
     * @param y Y value of the destination file.
     * @see org.vinsert.api.Walking#walk(org.vinsert.api.wrappers.interaction.SceneNode)
     */
    void walk(int x, int y);

    /**
     * Finds a path to specified x,y.
     *
     * @return null if no path found, otherwise array of tiles.
     */
    Tile[] findPath(int x, int y);

    /**
     * Checks if the specified scene node is reachable
     *
     * @param node node to navigate to
     * @return true if walkable
     */
    boolean canReach(SceneNode node);

    /**
     * Checks if the specified scene nodes can reach each other.
     *
     * @param from node to check from
     * @param to   node to check to
     * @return true if reachable
     */
    boolean canReach(SceneNode from, SceneNode to);

    /**
     * Checks if the specified tile is reachable
     *
     * @param node tile to navigate to
     * @return true if walkable
     */
    boolean canReach(Tile node);

    /**
     * Checks if the specified tiles can reach each other.
     *
     * @param from tile to check from
     * @param to   tile to check to
     * @return true if reachable
     */
    boolean canReach(Tile from, Tile to);

    /**
     * Clicks the Tile specified on the minimap.
     *
     * @param tile Tile to click.
     * @return true if clicked successfully
     */
    boolean clickOnMap(Tile tile);

}
