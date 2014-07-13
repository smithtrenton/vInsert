package org.vinsert.game.engine.extension;

import org.vinsert.api.event.BufferFlipEvent;
import org.vinsert.api.event.Events;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.core.Session;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An extended version of the canvas.
 */
public class CanvasExtension extends Canvas {
    private BufferedImage backBuffer;
    private BufferedImage gameBuffer;
    private final FakeGraphics graphics = new FakeGraphics();
    private int fps = 40;
    private Session session;

    public CanvasExtension() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        backBuffer = config.createCompatibleImage(765, 503);
        gameBuffer = config.createCompatibleImage(765, 503);
    }

    /**
     * Flips the graphics buffer, after
     * which it returns a clean one
     * for the applet to draw on.
     *
     * @return graphics
     */
    @Override
    public final Graphics getGraphics() {
        Graphics gameGraphics = gameBuffer.getGraphics();
        int time = fps > 0 ? 1000 / fps : 200;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (fps > 0) {
            // Draw the graphics buffer (bot itself)
            Graphics bufferGraphics = backBuffer.getGraphics();
            bufferGraphics.drawImage(gameBuffer, 0, 0, null);
            handleSession(bufferGraphics);
            Graphics base = super.getGraphics();
            base.drawImage(backBuffer, 0, 0, this);
            return gameGraphics;
        } else {
            handleSession(null);
            return graphics;
        }
    }

    /**
     * Handles all the drawing and linking for the session.
     *
     * @param bufferGraphics graphics
     */
    private void handleSession(Graphics bufferGraphics) {
        if (getParent() != null) {
            if (session == null) {
                session = Session.getSession(getParent().hashCode());
            } else {
                Events eventBus = session.getEnvironment().getEventBus();
                fps = session.getEnvironment().getFramesPerSecond();
                if (bufferGraphics != null) {
                    eventBus.submit(new PaintEvent(bufferGraphics));
                    eventBus.submit(new BufferFlipEvent(gameBuffer));
                }
            }
        }
    }

    @Override
    public final void setBounds(int x, int y, int width, int height) {
        super.setBounds(0, 0, width, height);
    }

    public final synchronized BufferedImage getBackBuffer() {
        return backBuffer;
    }


    public final synchronized BufferedImage getGameBuffer() {
        return gameBuffer;
    }
}
