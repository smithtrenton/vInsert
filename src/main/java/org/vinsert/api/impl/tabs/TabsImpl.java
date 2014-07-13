package org.vinsert.api.impl.tabs;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Tabs;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Tab;
import org.vinsert.api.wrappers.Widget;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * API implementation for interaction with RuneScape tabs.
 */
public final class TabsImpl implements Tabs {
    private final MethodContext ctx;
    private static final int GROUP_ID = 548;

    @Inject
    public TabsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean open(final Tab tab) {
        if (getCurrent() != tab) {
            Widget widget = ctx.widgets.find(GROUP_ID, tab.getId()).single();
            if (widget != null) {
                widget.click(true);
                Utilities.sleepUntil(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return getCurrent() == tab;
                    }
                }, 2000);
            }
        }
        return getCurrent() == tab;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen(Tab tab) {
        return getCurrent() == tab;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tab getCurrent() {
        return getOpen();
    }

    private Widget[] getAll() {
        Widget[] tabs = new Widget[Tab.values().length];
        for (int i = 0; i < tabs.length; i++) {
            tabs[i] = ctx.widgets.find(GROUP_ID, Tab.values()[i].getId()).single();
        }
        return tabs;
    }

    private Tab getOpen() {
        Widget[] all = getAll();
        int targetTextureID = getOpenTextureID(all);
        for (Widget widget : all) {
            if (widget.getTextureId() == targetTextureID) {
                return Tab.byId(widget.getId());
            }
        }
        return null;
    }

    private int getOpenTextureID(Widget[] widgets) {
        int[] textureIDs = new int[widgets.length];
        Map<Integer, Integer> frequency = new HashMap<Integer, Integer>();
        for (int i = 0; i < widgets.length; i++) {
            textureIDs[i] = widgets[i].getTextureId();
            if (frequency.containsKey(textureIDs[i])) {
                int freq = frequency.get(textureIDs[i]);
                frequency.remove(textureIDs[i]);
                frequency.put(textureIDs[i], freq + 1);
            } else {
                frequency.put(textureIDs[i], 1);
            }
        }
        return extractSingleton(frequency, -1);
    }

    private <T> T extractSingleton(Map<T, Integer> items, T standard) {
        for (Map.Entry<T, Integer> entries : items.entrySet()) {
            if (entries.getValue() == 1) {
                return entries.getKey();
            }
        }
        return standard;
    }

    public void logout() {
        if (ctx.client.getLoginIndex() == 30) {
            if (ctx.player.isInCombat()) {
                Utilities.sleepUntil(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return !ctx.player.isInCombat();
                    }
                }, 15000);
            }
            if (getCurrent() != Tab.LOGOUT) {
                open(Tab.LOGOUT);
                Utilities.sleepUntil(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return getCurrent() == Tab.LOGOUT;
                    }
                }, 5000);
                ctx.mouse.click(new Point(644, 379), true);
            }
        }
    }
}
