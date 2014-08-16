package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Minimap;
import org.vinsert.api.impl.LocalPlayer;
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
        int xMap = x - ctx.client.getBaseX();
        int yMap = y - ctx.client.getBaseY();
        LocalPlayer myPlayer = ctx.player;
        x = (xMap * 4 + 2) - myPlayer.getLocalX() / 32;
        y = (yMap * 4 + 2) - myPlayer.getLocalY() / 32;
        int mapScale = ctx.client.getMinimapScale();
        int mapOffset = ctx.client.getMinimapOffset();
        int angle = ctx.client.getMinimapAngle() + mapScale & 0x7FF;
        int j = x * x + y * y;
        if (j > 6400)
            return new Point(-1, -1);
        int sin = ctx.client.getSineTable()[angle] * 256 / (mapOffset + 256);
        int cos = ctx.client.getCosineTable()[angle] * 256 / (mapOffset + 256);
        xMap = y * sin + x * cos >> 16;
        yMap = y * cos - x * sin >> 16;
        return new Point(644 + xMap, 80 - yMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getWidget() {
        return ctx.widgets.find().id(MINIMAP_INTERFACE_GROUP, MINIMAP_INTERFACE_CHILD).single();
    }
}
