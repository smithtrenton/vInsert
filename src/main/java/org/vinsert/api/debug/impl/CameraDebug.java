package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.core.Session;

import java.awt.*;

/**
 *
 */
public final class CameraDebug extends AbstractDebug {
    public CameraDebug(Session session) {
        super(session);
    }

    @Override
    public String getName() {
        return "Camera";
    }

    @Override
    public String getShortcode() {
        return "camera";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.CYAN);
        g.drawString(String.format("X: %d, Y: %d, Z: %d, Pitch: %d, Yaw: %d",
                camera.getX(), camera.getY(), camera.getZ(),
                client.getCameraPitch(),
                client.getCameraYaw()),
                10, 100
        );
    }
}
