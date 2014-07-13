package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.core.Session;

import java.awt.*;
import java.util.List;

/**
 *
 */
public final class BoundaryObjectDebug extends AbstractDebug {

    public BoundaryObjectDebug(Session session) {
        super(session);
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.YELLOW);
        if (isLoggedIn()) {
            for (GameObject object : objects.find().distance(10).type(GameObject.GameObjectType.BOUNDARY).asList()) {
                Point pos = object.getBasePoint();
                g.drawString(String.valueOf(object.getId()), pos.x, pos.y);
            }

            List<GameObject> boundariesOnTile = objects.find().location(player.getTile()).type(GameObject.GameObjectType.BOUNDARY).asList();
            for (int i = 0; i < boundariesOnTile.size(); i++) {
                GameObject boundary = boundariesOnTile.get(i);
                g.drawString("Boundary on tile: " + boundary.getName(), 10, (150 + (i * 30)));
            }
        }
    }

    @Override
    public String getName() {
        return "Objects/Boundaries";
    }

    @Override
    public String getShortcode() {
        return "boundaries";
    }
}
