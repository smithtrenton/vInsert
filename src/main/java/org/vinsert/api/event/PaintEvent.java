package org.vinsert.api.event;

import java.awt.*;

/**
 * @author const_
 */
public final class PaintEvent {

    private Graphics graphics;

    public PaintEvent(Graphics graphics) {
        this.graphics = graphics;
    }

    public Graphics getGraphics() {
        return graphics;
    }
}
