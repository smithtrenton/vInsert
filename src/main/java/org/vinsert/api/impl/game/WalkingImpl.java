package org.vinsert.api.impl.game;

import com.google.gson.annotations.SerializedName;
import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Walking;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.impl.walking.Path;
import org.vinsert.api.impl.walking.impl.RSRegionPathFinder;
import org.vinsert.api.util.DynamicPoint;
import org.vinsert.api.util.MouseCallback;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.Player;
import org.vinsert.api.wrappers.Tile;
import org.vinsert.api.wrappers.interaction.SceneNode;
import org.vinsert.core.Session;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;


/**
 * Date: 27/08/13
 * Time: 13:10
 *
 * @author Matt Collinge
 * @see org.vinsert.api.Walking
 */
public final class WalkingImpl implements Walking {
    private static final Logger logger = Logger.getLogger(WalkingImpl.class);
    private final MethodContext ctx;

    @Inject
    public WalkingImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void traverse(Tile[] path, Direction direction) {
        try {
            Tile target = direction == Direction.FORWARDS ? path[path.length - 1] : path[0];
            if (direction == Direction.BACKWARDS) {
                path = reversePath(path);
            }

            int attemptsMade = 0;
            while (ctx.players.getLocal().getTile().distanceTo(target) > 3
                    && attemptsMade < 10 && ctx.session.getState() != Session.State.STOPPED) {
                Tile next = next(path, 10);
                if (next != null) {
                    boolean status = clickOnMap(next);
                    if (!status) {
                        attemptsMade++;
                    }
                } else {
                    break;
                }
                Utilities.sleep(10);
            }
            clickOnMap(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void walk(SceneNode destination) {
        walk(destination.getX(), destination.getY());
    }

    @Override
    public void walk(Tile tile) {
        walk(tile.getX(), tile.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void walk(int x, int y) {
        Point current = ctx.minimap.convert(x, y);
        if (current.x < 0 || current.y < 0) {
            if (!walkLocal(x, y)) {
                logger.info("Local walking failed..");
            }
        } else {
            Tile tile = new Tile(x, y);
            clickOnMap(tile);
        }
    }

    private boolean walkLocal(int x, int y) {
        RSRegionPathFinder pf = new RSRegionPathFinder(ctx);
        Path path = pf.getPath(x, y, RSRegionPathFinder.FULL);
        if (path != null && path.getLength() != 0) {
            ctx.logger.log(Level.INFO, "Walking to " + x + ", " + y + " (LOCAL|" + path.getLength() + " steps.)");
            final Tile[] tiles = path.toTiles(1);
            traverse(tiles, Direction.FORWARDS);
        } else {
            ctx.logger.log(Level.SEVERE, "Local path not found to " + x + ", " + y);
            return false;
        }
        return true;
    }

    public Tile[] findPath(int destX, int destY) {
        RSRegionPathFinder pf = new RSRegionPathFinder(ctx);
        Path path = pf.getPath(destX, destY, RSRegionPathFinder.FULL);
        if (path != null && path.getLength() != 0) {
            return path.toTiles(1);
        } else {
            ctx.logger.log(Level.SEVERE, "Local path not found to " + destX + ", " + destY);
        }
        return null;
    }


    /**
     * {@inheritDoc}
     */
    public boolean canReach(SceneNode node) {
        Player me = ctx.players.getLocal();
        return canReach(me.getLocalX() >> 7, me.getLocalY() >> 7,
                node.getLocalX() >> 7, node.getLocalY() >> 7,
                node instanceof GameObject);
    }

    @Override
    public boolean canReach(SceneNode from, SceneNode to) {
        return canReach(from.getLocalX() >> 7, from.getLocalY() >> 7,
                to.getLocalX() >> 7, to.getLocalY() >> 7,
                to instanceof GameObject);
    }

    @Override
    public boolean canReach(Tile node) {
        return canReach(ctx.player.getTile(), node);
    }

    @Override
    public boolean canReach(Tile from, Tile to) {
        return canReach(getLocalX(from.getX()) >> 7, getLocalY(from.getY()) >> 7,
                getLocalX(to.getX()) >> 7, getLocalY(to.getY()) >> 7,
                false);
    }

    private int getLocalX(int x) {
        return (x - ctx.client.getBaseX()) * 128;
    }

    private int getLocalY(int y) {
        return (y - ctx.client.getBaseY()) * 128;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clickOnMap(final Tile tile) {
        final Point m = ctx.minimap.convert(getReachableVariant(tile));
        if (m.x != -1 || (getClosestOnMap(tile) != null && clickOnMap(getClosestOnMap(tile)))) {
            ctx.keyboard.hold(KeyEvent.VK_CONTROL);
            return ctx.mouse.moveDynamic(
                    new DynamicPoint() {
                        @Override
                        public Point getPoint() {
                            return ctx.minimap.convert(tile);
                        }
                    },
                    new MouseCallback() {
                        @Override
                        public boolean onComplete() {
                            ctx.mouse.click(true);
                            ctx.keyboard.release(KeyEvent.VK_CONTROL);
                            Utilities.sleepUntil(walking(), 600);
                            if (ctx.players.getLocal().isMoving()) {
                                Utilities.sleepWhile(walking(tile, 2), 7500);
                                return true;
                            }
                            return false;
                        }
                    }
            );
        } else {
            ctx.logger.log("Tile is off screen " + this);
            ctx.logger.log("Current position " + ctx.players.getLocal().getTile());
        }
        return false;
    }

    private Tile getReachableVariant(Tile tile) {
        if (ctx.walking.canReach(tile)) {
            return tile;
        }

        ArrayList<Tile> options = new ArrayList<Tile>();
        for (int i = -3; i < 3; i++) {
            for (int j = -3; j < 3; j++) {
                options.add(new Tile(tile.getX() + i, tile.getY() + j));
            }
        }

        for (Tile option : options) {
            if (!ctx.walking.canReach(option, tile)) {
                continue;
            } else if (option.equals(ctx.players.getLocal().getTile())) {
                continue;
            } else if (!ctx.walking.canReach(option)) {
                continue;
            }

            ctx.logger.log("Selected reachable variant " + option.toString());
            return option;
        }
        return tile;
    }

    private Tile getClosestOnMap(Tile tile) {
        if (ctx.minimap.convert(tile.getX(), tile.getY()).x != -1) {
            return tile;
        }
        for (int dx = -10; dx < 10; dx++) {
            for (int dy = -10; dy < 10; dy++) {
                Tile relative = new Tile(tile.getX() + dx, tile.getY() + dy, 0);
                Point minimap = ctx.minimap.convert(relative);
                if (minimap.x != -1 && minimap.y != -1) {
                    return relative;
                }
            }
        }
        return null;
    }

    private StatePredicate walking() {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return ctx.players.getLocal().isMoving();
            }
        };
    }

    private StatePredicate walking(final Tile tile, final int distance) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return ctx.players.getLocal().isMoving() && tile.distanceTo(ctx.players.getLocal().getTile()) > distance;
            }
        };
    }

    /**
     * Gets the next best tile in the path to walk to.
     *
     * @param path        Tile array that is the path.
     * @param maxDistance Max distance away the next tile should be.
     * @return next Tile or null.
     */
    private Tile next(Tile[] path, int maxDistance) {
        Tile cur = ctx.players.getLocal().getTile();
        for (int i = path.length - 1; i >= 0; i--) {
            if (cur.distanceTo(path[i]) <= maxDistance
                    && cur.distanceTo(path[path.length - 1]) > 7) {
                return path[i];
            }
        }
        return null;
    }

    /**
     * Reverses an array of tiles
     *
     * @param path Path to reverse
     * @return reversed path
     */
    private Tile[] reversePath(Tile[] path) {
        Tile temp;
        for (int start = 0, end = path.length - 1; start < end; start++, end--) {
            temp = path[start];
            path[start] = path[end];
            path[end] = temp;
        }
        return path;
    }

    /**
     * Internal can reach for the local region.
     *
     * @param startX   Starting X co-ordinate, usually player position.
     * @param startY   Starting Y co-ordinate, usually player position.
     * @param destX    Destination X co-ordinate.
     * @param destY    Destination Y co-ordinate.
     * @param isObject If the destination is a GameObject we need to handle that.
     * @return true if player can walk to the destination.
     */
    private boolean canReach(int startX, int startY, int destX,
                             int destY, boolean isObject) {
        if (startX > 103 || startY > 103 || startX < 0 || startY < 0) {
            return false;
        }
        // Documentation part:
        // The blocks info
        // When you can walk freely it's 0, also used to create a noclip
        int[][] via = new int[104][104];
        int[][] cost = new int[104][104];
        int[] tileQueueX = new int[4000];
        int[] tileQueueY = new int[4000];

        for (int xx = 0; xx < 104; xx++) {
            for (int yy = 0; yy < 104; yy++) {
                via[xx][yy] = 0;
                cost[xx][yy] = 99999999;
            }
        }

        int curX = startX;
        int curY = startY;
        via[startX][startY] = 99;
        cost[startX][startY] = 0;
        int head = 0;
        int tail = 0;
        tileQueueX[head] = startX;
        tileQueueY[head] = startY;
        head++;
        int pathLength = tileQueueX.length;
        int blocks[][] = ctx.client.getMaps()[ctx.client.getPlane()].getFlags();
        while (tail != head) {
            curX = tileQueueX[tail];
            curY = tileQueueY[tail];

            if (!isObject && curX == destX && curY == destY) {
                return true;
            } else if (isObject
                    && ((curX == destX && curY == destY + 1)
                    || (curX == destX && curY == destY - 1)
                    || (curX == destX + 1 && curY == destY)
                    || (curX == destX - 1 && curY == destY))) {
                return true;
            }
            tail = (tail + 1) % pathLength;

            // Big and ugly block of code
            int thisCost = cost[curX][curY] + 1;
            // Can go south (by determining, whether the north side of the
            // south tile is blocked :P)
            if (curY > 0 && via[curX][curY - 1] == 0
                    && (blocks[curX][curY - 1] & 0x1280102) == 0) {
                tileQueueX[head] = curX;
                tileQueueY[head] = curY - 1;
                head = (head + 1) % pathLength;
                via[curX][curY - 1] = 1;
                cost[curX][curY - 1] = thisCost;
            }
            // Can go west
            if (curX > 0 && via[curX - 1][curY] == 0
                    && (blocks[curX - 1][curY] & 0x1280108) == 0) {
                tileQueueX[head] = curX - 1;
                tileQueueY[head] = curY;
                head = (head + 1) % pathLength;
                via[curX - 1][curY] = 2;
                cost[curX - 1][curY] = thisCost;
            }
            // Can go north
            if (curY < 104 - 1 && via[curX][curY + 1] == 0
                    && (blocks[curX][curY + 1] & 0x1280120) == 0) {
                tileQueueX[head] = curX;
                tileQueueY[head] = curY + 1;
                head = (head + 1) % pathLength;
                via[curX][curY + 1] = 4;
                cost[curX][curY + 1] = thisCost;
            }
            // Can go east
            if (curX < 104 - 1 && via[curX + 1][curY] == 0
                    && (blocks[curX + 1][curY] & 0x1280180) == 0) {
                tileQueueX[head] = curX + 1;
                tileQueueY[head] = curY;
                head = (head + 1) % pathLength;
                via[curX + 1][curY] = 8;
                cost[curX + 1][curY] = thisCost;
            }
            // Can go southwest
            if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0
                    && (blocks[curX - 1][curY - 1] & 0x128010e) == 0
                    && (blocks[curX - 1][curY] & 0x1280108) == 0
                    && (blocks[curX][curY - 1] & 0x1280102) == 0) {
                tileQueueX[head] = curX - 1;
                tileQueueY[head] = curY - 1;
                head = (head + 1) % pathLength;
                via[curX - 1][curY - 1] = 3;
                cost[curX - 1][curY - 1] = thisCost;
            }
            // Can go northwest
            if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0
                    && (blocks[curX - 1][curY + 1] & 0x1280138) == 0
                    && (blocks[curX - 1][curY] & 0x1280108) == 0
                    && (blocks[curX][curY + 1] & 0x1280120) == 0) {
                tileQueueX[head] = curX - 1;
                tileQueueY[head] = curY + 1;
                head = (head + 1) % pathLength;
                via[curX - 1][curY + 1] = 6;
                cost[curX - 1][curY + 1] = thisCost;
            }
            // Can go southeast
            if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0
                    && (blocks[curX + 1][curY - 1] & 0x1280183) == 0
                    && (blocks[curX + 1][curY] & 0x1280180) == 0
                    && (blocks[curX][curY - 1] & 0x1280102) == 0) {
                tileQueueX[head] = curX + 1;
                tileQueueY[head] = curY - 1;
                head = (head + 1) % pathLength;
                via[curX + 1][curY - 1] = 9;
                cost[curX + 1][curY - 1] = thisCost;
            }
            // can go northeast
            if (curX < 104 - 1 && curY < 104 - 1
                    && via[curX + 1][curY + 1] == 0
                    && (blocks[curX + 1][curY + 1] & 0x12801e0) == 0
                    && (blocks[curX + 1][curY] & 0x1280180) == 0
                    && (blocks[curX][curY + 1] & 0x1280120) == 0) {
                tileQueueX[head] = curX + 1;
                tileQueueY[head] = curY + 1;
                head = (head + 1) % pathLength;
                via[curX + 1][curY + 1] = 12;
                cost[curX + 1][curY + 1] = thisCost;
            }
        }
        return false;
    }

    /**
     * PathRequestPacket wrapper, for internal use only.
     */
    @Deprecated
    public static class PathRequest {
        @SerializedName("class")
        public String clazz = "ms.aurora.sdn.model.PathRequestPacket";
        public int currentPlane = 0;
        public int currX;
        public int currY;
        public int destX;
        public int destY;

        public PathRequest(int currX, int currY, int destX, int destY) {
            this.currX = currX;
            this.currY = currY;
            this.destX = destX;
            this.destY = destY;
        }

        public PathRequest() {
        }

    }
}
