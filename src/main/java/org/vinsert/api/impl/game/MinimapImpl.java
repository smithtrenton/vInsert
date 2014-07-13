package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Minimap;
import org.vinsert.api.wrappers.Tile;
import org.vinsert.api.wrappers.Widget;

import java.awt.*;

/**
 * Date: 27/08/13
 * Time: 14:17
 *
 * @author Matt Collinge
 * @see org.vinsert.api.Minimap
 */
public final class MinimapImpl implements Minimap {
    private static final int MINIMAP_INTERFACE_GROUP = 548;
    private static final int MINIMAP_INTERFACE_CHILD = 71;

    private final MethodContext ctx;

    @Inject
    public MinimapImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(Tile tile) {
        return tile == null ? new Point(-1, -1) : convert(tile.getX(), tile.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(int x, int y) {
        Tile tile = new Tile(x, y);
        if (tile.distanceTo(ctx.player.getTile()) > 17) {
            return new Point(-1, -1);
        }

        x -= ctx.client.getBaseX();
        y -= ctx.client.getBaseY();
        Widget mm = getWidget();
        if (mm != null) {
            final int xx = x * 4 + 2 - ctx.players.getLocal().getLocalX() / 32;
            final int yy = 2 + y * 4 - ctx.players.getLocal().getLocalY() / 32;

            int degree = ctx.client.getMinimapScale() + ctx.client.getMinimapAngle() & 0x7FF;
            int dist = (int) (Math.pow(xx, 2) + Math.pow(yy, 2));

            if (dist <= 6400) {
                int sin = ctx.client.getSineTable()[degree];
                int cos = ctx.client.getCosineTable()[degree];

                cos = cos * 256 / (ctx.client.getMinimapOffset() + 256);
                sin = sin * 256 / (ctx.client.getMinimapOffset() + 256);

                int mx = yy * sin + cos * xx >> 16;
                int my = sin * xx - yy * cos >> 16;
                final int screenx = 18 + ((mm.getX() + mm.getWidth() / 2) + mx);
                final int screeny = (mm.getY() + mm.getWidth() / 2 - 1) + my;
                if (mm.getArea().contains(screenx, screeny) && (Math.max(my, -my) <= (((mm.getWidth()) / 2.0) * .8))
                        && (Math.max(mx, -mx) <= (((mm
                        .getHeight()) / 2) * .8))) {
                    return new Point(screenx, screeny);
                }
            }
        }
        return new Point(-1, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convertUnbounded(Tile tile) {
        return tile == null ? new Point(-1, -1) : convertUnbounded(tile.getX(), tile.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convertUnbounded(int x, int y) {
        x -= ctx.client.getBaseX();
        y -= ctx.client.getBaseY();
        Widget mm = ctx.widgets.find().id(MINIMAP_INTERFACE_GROUP, MINIMAP_INTERFACE_CHILD).single();
        if (mm != null) {
            final int xx = x * 4 + 2 - ctx.players.getLocal().getLocalX() / 32;
            final int yy = 2 + y * 4 - ctx.players.getLocal().getLocalY() / 32;

            int degree = ctx.client.getMinimapScale() + ctx.client.getMinimapAngle() & 0x7FF;
            int dist = (int) (Math.pow(xx, 2) + Math.pow(yy, 2));

            int sin = ctx.client.getSineTable()[degree];
            int cos = ctx.client.getCosineTable()[degree];

            cos = cos * 256 / (ctx.client.getMinimapOffset() + 256);
            sin = sin * 256 / (ctx.client.getMinimapOffset() + 256);

            int mx = yy * sin + cos * xx >> 16;
            int my = sin * xx - yy * cos >> 16;
            if (dist < 2500) {

                final int sx = 18 + ((mm.getX() + mm.getHeight() / 2) + mx);
                final int sy = (mm.getY() + mm.getHeight() / 2 - 1) + my;

                return new Point(sx, sy);
            }

            final int screenx = 18 + ((mm.getX() + mm.getWidth() / 2) + mx);
            final int screeny = (mm.getY() + mm.getWidth() / 2 - 1) + my;

            return new Point(screenx, screeny);
        }
        return new Point(-1, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getWidget() {
        return ctx.widgets.find().id(MINIMAP_INTERFACE_GROUP, MINIMAP_INTERFACE_CHILD).single();
    }
}
