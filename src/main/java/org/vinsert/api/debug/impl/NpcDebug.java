package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.wrappers.Npc;
import org.vinsert.core.Session;

import java.awt.*;
import java.util.List;

/**
 *
 */
public final class NpcDebug extends AbstractDebug {

    public NpcDebug(Session session) {
        super(session);
    }

    @Override
    public String getName() {
        return "NPCs";
    }

    @Override
    public String getShortcode() {
        return "npcs";
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics graphics = event.getGraphics();
        if (isLoggedIn()) {
            List<Npc> all = npcs.find().distance(10).asList();
            for (Npc npc : all) {
                Point screen = npc.getBasePoint();
                graphics.setColor(Color.MAGENTA);
                int modelId = npc.getComposite().getModelIds() != null
                        ? npc.getComposite().getModelIds()[0] : -1;
                String info = String.format("%s (%d) | M: %d | A: %d | I: %d | WQL: %d | V: %d",
                        npc.getName(), npc.getId(),
                        modelId,
                        npc.getAnimationId(),
                        npc.getAssociatedEntity(),
                        npc.getQueueSize(),
                        npc.getModel() == null ? -1 : npc.getModel().getPolygons().length);
                graphics.drawString(info, screen.x, screen.y);
            }
        }
    }
}
