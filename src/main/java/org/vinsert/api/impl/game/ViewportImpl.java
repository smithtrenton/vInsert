package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.Constants;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Viewport;
import org.vinsert.api.wrappers.Tile;

import java.awt.*;

/**
 * API implementation for dealing with viewport operations.
 */
public final class ViewportImpl implements Viewport {
    private static final Rectangle VIEWPORT = new Rectangle(4, 4, 512, 334);
    private final MethodContext ctx;

    @Inject
    public ViewportImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(Tile tile) {
        return convertLocal(new Tile(getLocalX(tile.getX()), getLocalY(tile.getY())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(int x, int y, int modelHeight) {
        if (x < 128 || y < 128 || x > 13056 || y > 13056) {
            return new Point(-1, -1);
        }

        int z = getTileHeight(x, y, ctx.client.getPlane()) - modelHeight;
        x -= ctx.client.getCameraX();
        z -= ctx.client.getCameraZ();
        y -= ctx.client.getCameraY();

        int sinCurveY = ctx.client.getSineTable()[ctx.client.getCameraPitch()];
        int cosCurveY = ctx.client.getCosineTable()[ctx.client.getCameraPitch()];
        int sinCurveX = ctx.client.getSineTable()[ctx.client.getCameraYaw()];
        int cosCurveX = ctx.client.getCosineTable()[ctx.client.getCameraYaw()];
        int calculation = sinCurveX * y + cosCurveX * x >> 16;
        y = y * cosCurveX - x * sinCurveX >> 16;
        x = calculation;
        calculation = cosCurveY * z - sinCurveY * y >> 16;
        y = sinCurveY * z + cosCurveY * y >> 16;
        z = calculation;
        if (y >= 50) {
            int screenX = ((x << 9) / y + 258);
            int screenY = ((z << 9) / y + 169);
            if (VIEWPORT.contains(screenX, screenY)) {
                return new Point(screenX, screenY);
            }
        }

        return new Point(-1, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convert(int tileX, int tileY, double offsetX, double offsetY, int height) {
        return convert((int) ((tileX - ctx.client.getBaseX() + offsetX) * 128), (int) ((tileY
                - ctx.client.getBaseY() + offsetY) * 128), height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point convertLocal(Tile tile) {
        return convert(tile.getX(), tile.getY(), tile.getZ());
    }

    /**
     * Calculates the tile height for a specified tile
     *
     * @param tileX     raw local X of the tile
     * @param tileY     raw local Y of the tile
     * @param tilePlane current client plane
     * @return tile height
     */
    private int getTileHeight(int tileX, int tileY, int tilePlane) {
        int x = tileX >> Constants.LOCAL_XY_SHIFT;
        int y = tileY >> Constants.LOCAL_XY_SHIFT;
        if (x < 0 || y < 0 || x > 103 || y > 103) {
            return 0;
        }
        int plane = tilePlane;
        if (plane < 3 && (ctx.client.getTileSettings()[1][x][y] & 0x2) == 2) {
            plane++;
        }
        int x2 = tileX & 0x7f;
        int y2 = tileY & 0x7f;
        int i = (((128 - x2)
                * ctx.client.getTileHeights()[plane][x][y] +
                ctx.client.getTileHeights()[plane][x + 1][y] * x2) >> Constants.LOCAL_XY_SHIFT);
        int j = ((ctx.client.getTileHeights()[plane][x][y + 1]
                * (128 - x2) + x2
                * ctx.client.getTileHeights()[plane][1 + x][y + 1]) >> Constants.LOCAL_XY_SHIFT);
        return (128 - y2) * i + y2 * j >> Constants.LOCAL_XY_SHIFT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInViewport(Point point) {
        return VIEWPORT.contains(point);
    }


    private int getLocalX(int x) {
        return (x - ctx.client.getBaseX()) * 128;
    }

    private int getLocalY(int y) {
        return (y - ctx.client.getBaseY()) * 128;
    }

}
