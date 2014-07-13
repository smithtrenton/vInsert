package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.wrappers.Player;
import org.vinsert.core.Session;

import java.awt.*;

/**
 *
 */
public final class PlayerDebug extends AbstractDebug {

    public PlayerDebug(Session session) {
        super(session);
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.YELLOW);
        if (isLoggedIn()) {
            for (Player player : players.find().distance(10).asList()) {
                Point pos = player.getBasePoint();
                String info = String.format("%s | A: %d | I: %d | WQL: %d",
                        player.getName(), player.getAnimationId(),
                        player.getAssociatedEntity(),
                        player.getQueueSize());
                g.drawString(info, pos.x, pos.y);

            }
        }
    }


    @Override
    public String getName() {
        return "Players";
    }

    @Override
    public String getShortcode() {
        return "players";
    }
}
