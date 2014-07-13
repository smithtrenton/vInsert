package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.core.Session;

import java.awt.*;

/**
 *
 */
public final class InteractableObjectDebug extends AbstractDebug {
    public InteractableObjectDebug(Session session) {
        super(session);
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.YELLOW);
        if (isLoggedIn()) {
            for (GameObject object : objects.find().distance(10).type(GameObject.GameObjectType.INTERACTABLE).asList()) {
                Point pos = object.getBasePoint();
                if (object.getComposite() != null) {
                    g.drawString(object.getId() + ":" + object.getComposite().getName(), pos.x, pos.y);
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Objects/Interactable Objects";
    }

    @Override
    public String getShortcode() {
        return "interactable_objects";
    }
}
