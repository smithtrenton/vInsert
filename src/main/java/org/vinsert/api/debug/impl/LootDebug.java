package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.wrappers.Loot;
import org.vinsert.core.Session;

import java.awt.*;

/**
 * @author : const_
 */
public final class LootDebug extends AbstractDebug {

    public LootDebug(Session session) {
        super(session);
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        if (isLoggedIn()) {
            for (Loot loot : this.loot.find().distance(10).asList()) {
                if (loot.getModel() != null) {
                    loot.getModel().draw(g);
                }
                g.drawString(String.valueOf(loot.getId()), loot.getBasePoint().x, loot.getBasePoint().y);
            }
        }
    }

    @Override
    public String getName() {
        return "Loot";
    }

    @Override
    public String getShortcode() {
        return "loot";
    }
}
