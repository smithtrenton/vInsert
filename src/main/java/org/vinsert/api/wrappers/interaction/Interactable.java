package org.vinsert.api.wrappers.interaction;


import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.util.Utilities;

import java.awt.*;

/**
 * A base-class for interactable objects within the game world.
 */
public abstract class Interactable {
    public MethodContext context;

    public Interactable(MethodContext context) {
        this.context = context;
    }

    public Interactable() {
    }

    /**
     * Gets the base point for the tile on which the interactable is positioned.
     *
     * @return base point
     */
    public abstract Point getBasePoint();

    /**
     * Gets the click point for the model of the interactable.
     *
     * @return click point
     */
    public abstract Point getClickPoint();

    /**
     * Checks if the interactable is still valid.
     *
     * @return valid
     */
    public abstract boolean isValid();

    /**
     * Sets a renderable flag for the interactable.
     * Currently works for:
     * - Players
     * - NPCs
     * - Objects
     *
     * @param allowed allowed
     */
    public abstract void setAllowed(boolean allowed);


    /**
     * Interacts with the interactable using the specified action regex.
     *
     * @param actionRegex Action regex.
     * @return result
     */
    public Result interact(String actionRegex) {
        Point clickPoint = getClickPoint();
        if (clickPoint.x == -1 && clickPoint.y == -1) {
            clickPoint = getBasePoint();
            if (clickPoint.x == -1 && clickPoint.y == -1) {
                return Result.NOT_ON_SCREEN;
            }
        }

        setAllowed(true);
        context.mouse.move(clickPoint);
        Utilities.sleep(110, 170);
        Utilities.sleepUntil(isInMenu(actionRegex), 1200);
        if (!context.menu.contains(actionRegex)) {
            return Result.NOT_IN_MENU;
        }
        Result result = context.menu.click(actionRegex);
        setAllowed(false);
        return result;
    }

    /**
     * Hovers over the interactable.
     */
    public void hover() {
        Point clickPoint = getClickPoint();
        if (clickPoint.x == -1 && clickPoint.y == -1) {
            clickPoint = getBasePoint();
            if (clickPoint.x == -1 && clickPoint.y == -1) {
                return;
            }
        }
        context.mouse.move(clickPoint);
    }

    /**
     * Hovers over the interactable while the predicate is true.
     */
    public void hoverUntil(StatePredicate predicate) {
        while (isValid() && predicate.apply()) {
            Point clickPoint = getClickPoint();
            if (clickPoint.x == -1 && clickPoint.y == -1) {
                clickPoint = getBasePoint();
                if (clickPoint.x == -1 && clickPoint.y == -1) {
                    return;
                }
            }
            context.mouse.move(clickPoint);
            Utilities.sleep(3, 10);
        }
    }

    /**
     * Clicks using #getClickPoint
     *
     * @param left <t>true to click left</t> false to click right
     */
    public void click(boolean left) {
        context.mouse.click(getClickPoint(), left);
    }

    /**
     * Checks if it is on screen
     *
     * @return <t>true if on screen</t> otherwise false
     */
    public boolean isOnScreen() {
        return getClickPoint() != null &&
                context.viewport.isInViewport(getClickPoint()) || getBasePoint() != null && context.viewport.isInViewport(getBasePoint());
    }

    public StatePredicate isInMenu(final String text) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return context.menu.contains(text);
            }
        };
    }

}
