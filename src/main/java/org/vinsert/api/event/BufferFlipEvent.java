package org.vinsert.api.event;

import java.awt.image.BufferedImage;

/**
 *
 */
public final class BufferFlipEvent {
    private BufferedImage lastBuffer;

    public BufferFlipEvent(BufferedImage lastBuffer) {
        this.lastBuffer = lastBuffer;
    }

    public BufferedImage getLastBuffer() {
        return lastBuffer;
    }

    public void setLastBuffer(BufferedImage lastBuffer) {
        this.lastBuffer = lastBuffer;
    }
}
