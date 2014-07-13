package org.vinsert.core.input.algorithm.impl;


import org.vinsert.core.input.algorithm.MousePathGenerator;

import java.awt.*;

/**
 *
 */
public final class StraightLineGenerator implements MousePathGenerator {

    @Override
    public Point[] generate(Point origin, Point dest) {
        double dist = origin.distance(dest);
        double xStep = (dest.x - origin.x) / dist;
        double yStep = (dest.y - origin.y) / dist;
        Point[] path = new Point[(int) dist];
        if (path.length > 0) {
            path[0] = origin;
            path[(int) dist - 1] = dest;
            for (int i = 1; i < dist - 1; i++) {
                path[i] = new Point((int) (origin.x + (xStep * i)), (int) (origin.y + (yStep * i)));
            }
        }
        return path;
    }

}
