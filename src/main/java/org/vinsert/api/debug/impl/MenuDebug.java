package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.core.Session;

import java.awt.*;

/**
 *
 */
public final class MenuDebug extends AbstractDebug {
    public MenuDebug(Session session) {
        super(session);
    }

    @Override
    public String getName() {
        return "Menu";
    }

    @Override
    public String getShortcode() {
        return "menu";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics graphics = event.getGraphics();
        graphics.setColor(Color.YELLOW);
        graphics.drawRect(client.getMenuX(), client.getMenuY(), 5, 5);
        for (int i = 0; i < menu.getLines().size(); i++) {
            graphics.drawString("Menu #" + i + ": " + menu.getLines().get(i), 10, 50 + (30 * i));
        }

    }
}
