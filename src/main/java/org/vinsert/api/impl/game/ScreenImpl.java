package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Screen;
import org.vinsert.core.control.ScriptManager;
import org.vinsert.game.engine.extension.CanvasExtension;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * API implementation for dealing with 'screen' operations.
 *
 * @see org.vinsert.api.Screen
 */
public final class ScreenImpl implements Screen {
    private final MethodContext ctx;

    @Inject
    public ScreenImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point> findColor(Color color, int tolerance) {
        List<Point> matches = new ArrayList<Point>();
        if (ctx.client.getCanvas() instanceof CanvasExtension) {
            CanvasExtension ext = (CanvasExtension) ctx.client.getCanvas();
            for (int x = 0; x < ext.getWidth(); x++) {
                for (int y = 0; y < ext.getHeight(); y++) {
                    Color colorAtPoint = new Color(ext.getGameBuffer().getRGB(x, y));
                    if (Math.abs(colorAtPoint.getRed() - color.getRed()) < tolerance &&
                            Math.abs(colorAtPoint.getGreen() - color.getGreen()) < tolerance &&
                            Math.abs(colorAtPoint.getBlue() - color.getBlue()) < tolerance) {
                        matches.add(new Point(x, y));
                    }
                }
            }
        }
        return matches;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void takeScreenCapture(String fileName) {
        final ScriptManager scriptManager = ctx.session.getScriptManager();
        if (ctx.client.getCanvas() instanceof CanvasExtension) {
            CanvasExtension ext = (CanvasExtension) ctx.client.getCanvas();
            BufferedImage buffer = ext.getBackBuffer();
            File screenshotsFolder = new File(System.getProperty("user.home") + File.separator + "vInsert"
                    + File.separator + "Screenshots" + File.separator +
                    scriptManager.getManifest().name() + File.separator);
            if (screenshotsFolder.exists() || screenshotsFolder.mkdirs()) {
                try {
                    ImageIO.write(buffer, "png", new File(screenshotsFolder, fileName + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
