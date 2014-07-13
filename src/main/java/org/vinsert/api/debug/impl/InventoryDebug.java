package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.wrappers.WidgetItem;
import org.vinsert.core.Session;

import java.awt.*;

/**
 *
 */
public final class InventoryDebug extends AbstractDebug {

    public InventoryDebug(Session session) {
        super(session);
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.ORANGE);
        if (isLoggedIn()) {
            for (WidgetItem item : inventory.getAll()) {
                Rectangle area = item.getArea();
                g.drawString(String.valueOf(item.getId()), area.x, area.y);
            }
        }
    }


    @Override
    public String getName() {
        return "Inventory";
    }

    @Override
    public String getShortcode() {
        return "inventory";
    }
}
