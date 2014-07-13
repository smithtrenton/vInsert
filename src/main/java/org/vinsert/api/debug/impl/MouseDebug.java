package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.debug.AutoEnable;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.core.Session;

import java.awt.*;

/**
 *
 */
@AutoEnable
public final class MouseDebug extends AbstractDebug {
    public MouseDebug(Session session) {
        super(session);
    }

    @Override
    public String getName() {
        return "Mouse";
    }

    @Override
    public String getShortcode() {
        return "mouse";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(new Color(1f, 0.1f, 0f, 0.7f));
        Point position = mouse.getPosition();
        g.drawLine(position.x - 5, position.y - 5, position.x + 5, position.y + 5);
        g.drawLine(position.x + 5, position.y - 5, position.x - 5, position.y + 5);
    }
}
