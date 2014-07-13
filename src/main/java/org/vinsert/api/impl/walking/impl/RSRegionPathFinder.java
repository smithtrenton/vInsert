package org.vinsert.api.impl.walking.impl;

import org.vinsert.api.MethodContext;
import org.vinsert.api.impl.walking.AStarPathFinder;
import org.vinsert.api.impl.walking.ClosestHeuristic;
import org.vinsert.api.impl.walking.Path;
import org.vinsert.game.engine.scene.ICollisionMap;

import java.awt.*;


/**
 *
 */
public final class RSRegionPathFinder {

    public static final int LAZY = 0;
    public static final int FULL = 1;
    private AStarPathFinder pathFinder;
    private final MethodContext ctx;


    public RSRegionPathFinder(MethodContext ctx) {
        this.ctx = ctx;
        reload();
    }

    private void reload() {
        ICollisionMap current = ctx.client.getMaps()[ctx.client.getPlane()];
        pathFinder = new AStarPathFinder(new RSRegion(ctx.client.getPlane(), current.getFlags()),
                200, true, new ClosestHeuristic(), ctx);
    }

    public Path getPath(int destX, int destY, int full) {
        reload();
        Point destPoint = getFixedPoint(destX, destY);
        return getPath(ctx.client.getPlane(), ctx.player.getLocalX() >> 7,
                ctx.player.getLocalY() >> 7,
                destPoint.x,
                destPoint.y, full);
    }

    private Point getFixedPoint(int x, int y) {
        x = x - ctx.client.getBaseX();
        y = y - ctx.client.getBaseY();

        if (x > 103) {
            x = 103;
        } else if (x < 0) {
            x = 0;
        }

        if (y > 103) {
            y = 103;
        } else if (y < 0) {
            y = 0;
        }
        return new Point(x, y);
    }

    public Path getPath(int plane, int startX, int startY, int destX, int destY, int full) {
        return pathFinder.findPath(plane, startX, startY, destX, destY, full);
    }
}
