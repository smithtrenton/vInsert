package org.vinsert.core.input.impl;

import org.vinsert.api.MethodContext;
import org.vinsert.api.util.Utilities;
import org.vinsert.core.input.InputChain;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Date: 29/08/13
 * Time: 09:40
 *
 * @author Matt Collinge
 */
public final class MouseInputChain extends InputChain<MouseEvent> {

    private long time;

    public MouseInputChain(MethodContext context) {
        super(context);
        time = System.currentTimeMillis();
    }

    /**
     * Utility method to convert button to button mask.
     *
     * @param button button to convert.
     * @return the corresponding mask.
     * @throws IllegalArgumentException
     */
    private int getButtonModifiers(int button) {
        switch (button) {
            case MouseEvent.BUTTON1:
                return MouseEvent.BUTTON1_MASK;
            case MouseEvent.BUTTON2:
                return MouseEvent.BUTTON2_MASK;
            case MouseEvent.BUTTON3:
                return MouseEvent.BUTTON3_MASK;
            default:
                throw new IllegalArgumentException("Invalid button");
        }
    }

    /**
     * Generates random time, used in clicking.
     *
     * @return random click time.
     */
    private int getRandomClickTime() {
        return 100 + Utilities.random(0, 60); // Blessed be this method by satan.
    }

    /**
     * Generates random time, used in moving.
     *
     * @return random move time.
     */
    private int getRandomMoveTime() {
        return 1 + Utilities.random(0, 3); // more of the devils handiwork
    }

    /**
     * Generates random time, used in dragging.
     *
     * @return random drag time.
     */
    private int getRandomDragTime() {
        return 3 + Utilities.random(0, 60); // his all over this shit, gonna need jesus to save us soon
    }

    /**
     * Adds a mouse press to the internal List.
     *
     * @param point  target point of the event.
     * @param button button to press.
     */
    public void press(Point point, int button) {
        int buttonModifiers = getButtonModifiers(button);
        int lag = getRandomClickTime();
        addEvent(new MouseEvent(getComponent(), MouseEvent.MOUSE_PRESSED, time, buttonModifiers, point.x, point.y, 1, false, button), lag);
        time += lag;
    }

    /**
     * Adds a mouse release to the internal List.
     *
     * @param point  target point of the event.
     * @param button button to release.
     */
    public void release(Point point, int button) {
        int buttonModifiers = getButtonModifiers(button);
        addEvent(new MouseEvent(getComponent(), MouseEvent.MOUSE_RELEASED, time, buttonModifiers, point.x, point.y, 1, false, button), 0);
    }

    /**
     * Adds a mouse drag to the internal List.
     *
     * @param points path to drag along.
     * @param button button to hold whilst dragging.
     */
    public void drag(Point[] points, int button) {
        int lag = getRandomDragTime();
        press(points[0], button);
        for (int i = 1; i < points.length - 1; i++) {
            addEvent(new MouseEvent(getComponent(), MouseEvent.MOUSE_DRAGGED, time, button, points[i].x, points[i].y, 0, false, 0), lag);
            lag += getRandomDragTime();
            time += lag;
        }
        release(points[points.length - 1], button);
    }

    /**
     * Adds a mouse move to the internal List.
     *
     * @param points path to drag along.
     */
    public void path(Point[] points) {
        int lag = 0;
        for (Point point : points) {
            addEvent(new MouseEvent(getComponent(), MouseEvent.MOUSE_MOVED, time, 0, point.x, point.y, 0, false, 0), lag);
            lag = getRandomMoveTime();
            time += lag;
        }
    }

    /**
     * Adds a mouse move to the internal List.
     *
     * @param points path to drag along.
     */
    public void pathQuick(Point[] points) {
        int lag = 0;
        for (Point point : points) {
            addEvent(new MouseEvent(getComponent(), MouseEvent.MOUSE_MOVED, time, 0, point.x, point.y, 0, false, 0), lag);
            lag = 0;
            time += lag;
        }
    }

    /**
     * Adds the number of mouse clicks specified to the internal List.
     *
     * @param p          target point of the click.
     * @param button     button to click.
     * @param clickCount number of times to click.
     */
    public void click(Point p, int button, int clickCount) {
        int buttonModifiers = getButtonModifiers(button);
        time += getRandomClickTime();
        int lag = 0;
        int count = 1;
        for (int i = 0; i < clickCount * 3; i += 3) {
            addEvent(new MouseEvent(getComponent(), MouseEvent.MOUSE_PRESSED, time, buttonModifiers, p.x, p.y, count, false, button), lag);
            lag = getRandomClickTime();
            time += lag;
            addEvent(new MouseEvent(getComponent(), MouseEvent.MOUSE_RELEASED, time, buttonModifiers, p.x, p.y, count, false, button), lag);
            addEvent(new MouseEvent(getComponent(), MouseEvent.MOUSE_CLICKED, time, buttonModifiers, p.x, p.y, count, false, button), lag);
            lag = getRandomClickTime();
            time += lag;
            count++;
        }
    }

}
