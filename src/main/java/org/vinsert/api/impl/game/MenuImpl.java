package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.Menu;
import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.interaction.Result;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

/**
 * API implementation for interacting with in-game menus.
 *
 * @author const_
 * @see org.vinsert.api.Menu
 */
public final class MenuImpl implements Menu {
    private final MethodContext ctx;

    @Inject
    public MenuImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex(String action) {
        for (String entry : getActions()) {
            Matcher matcher = Pattern.compile(action.toLowerCase()).matcher(entry);
            if (matcher.find()) {
                return getActions().indexOf(entry);
            }
        }

        for (String entry : getLines()) {
            Matcher matcher = Pattern.compile(action.toLowerCase()).matcher(entry);
            if (matcher.find()) {
                return getLines().indexOf(entry);
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(ctx.client.getMenuX(), ctx.client.getMenuY(),
                ctx.client.getMenuWidth(), ctx.client.getMenuHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return ctx.client.isMenuOpen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result click(String action) {
        ctx.keyboard.hold(KeyEvent.VK_CONTROL);
        Utilities.sleepUntil(containsPred(action), 1500);
        int itemIndex = getIndex(action);
        if (itemIndex != -1) {
            if (itemIndex == 0 || getHoverAction().equals(action)) {
                ctx.mouse.click(true);
                Utilities.sleepUntil(menuClosed, 2000);
                return (isOpen() ? Result.MISSED : Result.OK);
            } else {
                if (!isOpen()) {
                    ctx.mouse.click(false);
                    Utilities.sleepUntil(menuOpen, 2000);
                }
                if (isOpen()) {
                    ctx.mouse.click(ctx.client.getMenuX() + (Utilities.random(20, 20 + action.length() * 4)),
                            ctx.client.getMenuY() + (21 + (15 * itemIndex)), true);
                    Utilities.sleepUntil(menuClosed, 3000);
                    ctx.keyboard.release(KeyEvent.VK_CONTROL);
                    return (isOpen() ? Result.MISSED : Result.OK);
                }
            }

        }
        ctx.keyboard.release(KeyEvent.VK_CONTROL);
        return Result.NOT_IN_MENU;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(String action) {
        return getIndex(action) != -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHoverAction() {
        return getActions().get(0).toLowerCase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getLines() {
        String[] actions = ctx.client.getMenuOptions();
        String[] targets = ctx.client.getMenuActions();
        List<String> menuContent = newArrayList();
        for (int i = ctx.client.getMenuCount() - 1; i >= 0; i--) {
            if (actions[i] != null && targets[i] != null) {
                menuContent.add(format(actions[i] + " " + targets[i]).toLowerCase());
            }
        }
        return menuContent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getActions() {
        String[] actions = ctx.client.getMenuOptions();
        List<String> menuContent = newArrayList();
        for (int i = ctx.client.getMenuCount() - 1; i >= 0; i--) {
            if (actions[i] != null) {
                menuContent.add(format(actions[i]).toLowerCase());
            }
        }
        return menuContent;
    }

    /**
     * A simple helper method which standardizes the naming of menu items
     * Also prevents nullpointers
     *
     * @param input the input string
     * @return the formatted string
     */
    private String format(String input) {
        return input != null ? Pattern.compile("<.+?>").matcher(input).replaceAll("") : "null";
    }

    private final StatePredicate menuOpen = new StatePredicate() {
        @Override
        public boolean apply() {
            return isOpen();
        }
    };

    private final StatePredicate menuClosed = new StatePredicate() {
        @Override
        public boolean apply() {
            return !isOpen();
        }
    };

    private StatePredicate containsPred(final String args) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return contains(args);
            }
        };
    }
}
