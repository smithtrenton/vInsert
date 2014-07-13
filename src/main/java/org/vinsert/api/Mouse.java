package org.vinsert.api;

import org.vinsert.api.util.DynamicPoint;
import org.vinsert.api.util.MouseCallback;

import java.awt.*;

/**
 * Date: 29/08/13
 * Time: 09:29
 *
 * @author Matt Collinge
 */
public interface Mouse {

    /**
     * Gets the current position of the client mouse.
     *
     * @return current mouse position
     */
    Point getPosition();

    /**
     * Moves the mouse along a path to the Point.
     *
     * @param point Point to move mouse to.
     */
    void move(Point point);

    /**
     * Moves the mouse along a path to a dynamic point.
     *
     * @param point point
     */
    boolean moveDynamic(DynamicPoint point, MouseCallback callback);

    /**
     * @param x x co-ordinate of target.
     * @param y y co-ordinate of target.
     * @see org.vinsert.api.Mouse#move(java.awt.Point)
     */
    void move(int x, int y);

    /**
     * Clicks the mouse at its current location.
     *
     * @param left true to left click, false to right.
     */
    void click(boolean left);

    /**
     * Moves the mouse to the specified Point and clicks.
     *
     * @param point Target Point.
     * @param left  true to left click, false to right.
     */
    void click(Point point, boolean left);

    /**
     * @param x    x co-ordinate of target.
     * @param y    y co-ordinate of target.
     * @param left true to left click, false to right.
     * @see org.vinsert.api.Mouse#click(java.awt.Point, boolean)
     */
    void click(int x, int y, boolean left);

    /**
     * Drags the mouse from current position to specified Point using the specified button.
     *
     * @param button the mouse button to hold
     * @param point  target Point.
     */
    void drag(int button, Point point);

    /**
     * Drags the mouse from current position to specified Point using the specified button.
     *
     * @param button the mouse button to hold
     * @param x      the x coord
     * @param y      the y coord
     */
    void drag(int button, int x, int y);

    /**
     * Drags the mouse from current position to specified Point.
     *
     * @param point target Point.
     */
    void drag(Point point);

    /**
     * @param x x co-ordinate of target.
     * @param y y co-ordinate of target.
     * @see org.vinsert.api.Mouse#drag(java.awt.Point)
     */
    void drag(int x, int y);

    /**
     * Presses the mouse
     *
     * @param point - the point to press the mouse at
     */
    void press(Point point);

    /**
     * Releases the mouse (from being pressed)
     *
     * @param point - the point to release the mouse at
     */
    void release(Point point);

    void moveQuick(Point point);

    boolean moveDynamicQuick(DynamicPoint point, MouseCallback callback);
}
