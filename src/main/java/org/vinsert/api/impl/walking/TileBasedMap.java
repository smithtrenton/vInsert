package org.vinsert.api.impl.walking;

/**
 * Created by IntelliJ IDEA.
 * User: Johan
 * Date: 2010-feb-24
 * Time: 16:19:45
 * To change this template use File | Settings | File Templates.
 */
public interface TileBasedMap {
    /**
     * Get the width of the tile map. The slightly odd name is used
     * to distiguish this method from commonly used names in game maps.
     *
     * @return The number of tiles across the map
     */
    int getWidthInTiles();

    /**
     * Get the height of the tile map. The slightly odd name is used
     * to distiguish this method from commonly used names in game maps.
     *
     * @return The number of tiles down the map
     */
    int getHeightInTiles();

    /**
     * Notification that the path finder visited a given tile. This is
     * used for debugging new heuristics.
     *
     * @param x The x coordinate of the tile that was visited
     * @param y The y coordinate of the tile that was visited
     */
    void pathFinderVisited(int plane, int x, int y);

    boolean solid(int plane, int x, int y);

    int getDirection(int plane, int x, int y);

    int getBlock(int plane, int x, int y);

    boolean isWalkable(int plane, int sx, int sy, int tx, int ty);

    /**
     * Get the cost of moving through the given tile. This can be used to
     * make certain areas more desirable. A simple and valid implementation
     * of this method would be to return 1 in all cases.
     *
     * @param sx The x coordinate of the tile we're moving from
     * @param sy The y coordinate of the tile we're moving from
     * @param tx The x coordinate of the tile we're moving to
     * @param ty The y coordinate of the tile we're moving to
     * @return The relative cost of moving across the given tile
     */
    float getCost(int plane, int sx, int sy, int tx, int ty);
}
