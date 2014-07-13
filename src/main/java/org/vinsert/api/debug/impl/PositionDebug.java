package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.wrappers.Tile;
import org.vinsert.core.Session;

import java.awt.*;

/**
 *
 */
public final class PositionDebug extends AbstractDebug {

    public PositionDebug(Session session) {
        super(session);
    }

    @Override
    public String getName() {
        return "Position";
    }

    @Override
    public String getShortcode() {
        return "position";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        try {
            Graphics g = event.getGraphics();
            g.setColor(Color.RED);
            if (isLoggedIn()) {
                g.drawString("Tile: " + player.getTile().toString(), 10, 70);
                g.drawString("Base: " + client.getBaseX() + ", " + client.getBaseY(), 10, 100);
            } else {
                g.drawString("Tile: " + new Tile(0, 0).toString(), 10, 70);
                g.drawString("Base: " + client.getBaseX() + ", " + client.getBaseY(), 10, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
