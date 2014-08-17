package org.vinsert.gui.icons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

/**
 * Class for managing icon instances.
 */
public final class Icons {
    public static final ImageIcon ENABLED;
    public static final ImageIcon DISABLED;
    public static final ImageIcon PLAY;
    public static final ImageIcon STOP;
    public static final ImageIcon PAUSE;
    public static final ImageIcon HOME;
    public static final ImageIcon PLUS;
    public static final ImageIcon USER;
    public static final ImageIcon LOGO;
    public static final ImageIcon LOGO30;

    private Icons() {

    }

    static {
        try {
            USER = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/user.png")));
            PLUS = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/plus.png")));
            HOME = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/home2.png")));
            STOP = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/stop2.png")));
            PLAY = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/play3.png")));
            DISABLED = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/close.png")));
            ENABLED = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/checkmark.png")));
            PAUSE = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/pause2.png")));
            LOGO = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/logo.png")));
            LOGO30 = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/logo-30px.png")));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load icon..", e);
        }
    }

}
