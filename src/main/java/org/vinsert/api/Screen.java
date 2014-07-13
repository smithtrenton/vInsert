package org.vinsert.api;

import java.awt.*;

/**
 * API for getting information from the screen.
 */
public interface Screen {

    /**
     * Tries to find a color on the screen.
     *
     * @param color color
     * @return list of points with color
     */
    java.util.List<Point> findColor(Color color, int tolerance);

    /**
     * Takes a screen capture of the game buffer and saves it to
     * user.home/vInsert/Screenshots/{scriptName}/{fileName}
     *
     * @param fileName the file name
     */
    void takeScreenCapture(String fileName);

}
