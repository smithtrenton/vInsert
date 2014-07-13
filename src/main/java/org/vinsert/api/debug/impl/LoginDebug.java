package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.core.Session;

import java.awt.*;

/**
 * @author : const_
 */
public final class LoginDebug extends AbstractDebug {

    public LoginDebug(Session session) {
        super(session);
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.RED);
        g.drawString(String.format("Login Index: %d", client.getLoginIndex()), 10, 120);
    }

    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public String getShortcode() {
        return "login";
    }
}
