package org.vinsert.api;

import org.vinsert.api.wrappers.Tile;

import java.awt.*;

/**
 * Viewport methods
 */
public interface Viewport {

    /**
     * Converts a set of local coordinates into their screen positions
     *
     * @param tileX        local X coordinate
     * @param tileY        local Y coordinate
     * @param renderHeight Height of the content on the tile.
     * @return A point representing the screen coordinate, of which the x/y may be -1.
     */
    Point convert(int tileX, int tileY, int renderHeight);

    /**
     * Converts a tile to a on screen point
     *
     * @param tileX   the tile x
     * @param tileY   the tile y
     * @param offsetX the x offset
     * @param offsetY the y offset
     * @param height  the tile height
     * @return an on screen point
     */
    Point convert(int tileX, int tileY, double offsetX, double offsetY, int height);

    /**
     * Converts a world-wide tile to a regional tile and converts
     * that regional tile into a Point representing the position within the viewport.
     *
     * @param tile tile
     * @return A point representing the screen coordinate, of which the x/y may be -1.
     */
    Point convert(Tile tile);

    /**
     * Converts a regional tile to a screen position
     *
     * @param tile tile
     * @return A point representing the screen coordinate, of which the x/y may be -1.
     */
    Point convertLocal(Tile tile);

    /**
     * Tells you if a point is in the viewport.
     *
     * @param point The point to check
     * @return <t>true</t> if the point is within the viewport otherwise false.
     */
    boolean isInViewport(Point point);

}
