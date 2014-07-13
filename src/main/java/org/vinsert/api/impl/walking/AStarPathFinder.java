package org.vinsert.api.impl.walking;

import org.vinsert.api.MethodContext;
import org.vinsert.api.impl.walking.impl.RSRegion;
import org.vinsert.api.impl.walking.impl.RSRegionPathFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class AStarPathFinder {
    private Map<Long, Node> indexedNodeMap = new HashMap<Long, Node>();
    private List<Node> closed = new ArrayList<Node>();
    private SortedList open = new SortedList();
    private TileBasedMap map;
    int stackDepth = 0;

    private int maxSearchDistance;

    private boolean allowDiagMovement;

    private AStarHeuristic heuristic;
    private final MethodContext ctx;

    /**
     * Create a path finder with the default heuristic - closest to target.
     *
     * @param map               The map to be searched
     * @param maxSearchDistance The maximum depth we'll search before giving up
     * @param allowDiagMovement True if the search should try diaganol movement
     * @param ctx               method context
     */
    public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement, MethodContext ctx) {
        this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic(), ctx);
    }

    /**
     * Create a path finder
     *
     * @param heuristic         The heuristic used to determine the search order of the map
     * @param map               The map to be searched
     * @param maxSearchDistance The maximum depth we'll search before giving up
     * @param allowDiagMovement True if the search should try diaganol movement
     */
    public AStarPathFinder(TileBasedMap map, int maxSearchDistance,
                           boolean allowDiagMovement, AStarHeuristic heuristic, MethodContext ctx) {
        this.heuristic = heuristic;
        this.map = map;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;
        stackDepth = 0;
        this.ctx = ctx;
    }

    public Path findPath(int plane, int sx, int sy, int tx, int ty, int full) {
        if (map.solid(plane, sx, sy)) {
            for (int x = -3; x <= 3; x++) {
                for (int y = -3; y <= 3; y++) {
                    int mX = sx + x;
                    int mY = sy + y;

                    if (mX < map.getWidthInTiles() && mX > 0 && mY < map.getHeightInTiles() && mY > 0) {
                        if (!map.solid(plane, mX, mY)) {
                            return findPath(plane, mX, mY, tx, ty, full);
                        }
                    }
                }
            }
        }

        // easy first check, if the destination is blocked, we can't getAll there, find an adjacent tile instead
        if (map.solid(plane, tx, ty)) {
            for (int x = -3; x <= 3; x++) {
                for (int y = -3; y <= 3; y++) {
                    int mX = tx + x;
                    int mY = ty + y;

                    if (mX < map.getWidthInTiles() && mX > 0 && mY < map.getHeightInTiles() && mY > 0) {
                        if (!map.solid(plane, mX, mY)) {
                            return findPath(plane, sx, sy, mX, mY, full);
                        }
                    }
                }
            }
        }

        if (!(sx < map.getWidthInTiles() && sx > 0 && sy < map.getHeightInTiles() && sy > 0)) {
            return null;
        }
        nodes(sx, sy, new Node((short) sx, (short) sy));
        nodes(tx, ty, new Node((short) tx, (short) ty));

        // initial state for A*. The closed group is empty. Only the starting

        // tile is in the open list and it'e're already there
        nodes(sx, sy).cost = 0;
        nodes(sx, sy).depth = 0;
        closed.clear();
        open.clear();
        open.add(nodes(sx, sy));

        nodes(tx, ty).parent = null;

        // while we haven'n't exceeded our max search depth
        //System.out.println("Starting the search.");
        int maxDepth = 0;
        while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
            // pull out the first node in our open list, this is determined to
            // be the most likely to be the next step based on our heuristic

            Node current = getFirstInOpen();
            if (current == nodes(tx, ty)) {
                break;
            }

            removeFromOpen(current);
            addToClosed(current);

            // search through all the neighbours of the current node evaluating

            // them as next steps

            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    try {
                        // not a neighbour, its the current tile
                        if ((x == 0) && (y == 0)) {
                            continue;
                        }

                        // if we're not allowing diaganol movement then only

                        // one of x or y can be set

                        if (!allowDiagMovement) {
                            if ((x != 0) && (y != 0)) {
                                continue;
                            }
                        }

                        // determine the location of the neighbour and evaluate it

                        int xp = x + current.x;
                        int yp = y + current.y;
                        boolean isValid;

                        if (full == RSRegionPathFinder.FULL) {
                            isValid = isValidLocation(plane, current.x, current.y, xp, yp, map.getDirection(plane, x, y));
                        } else {
                            isValid = isValidLocation(plane, current.x, current.y, xp, yp, -1);
                        }

                        if (isValid) {
                            // the cost to getAll to this node is cost the current plus the movement

                            // cost to reach this node. Note that the heursitic value is only used

                            // in the sorted open list

                            float nextStepCost = current.cost + getMovementCost(plane, current.x, current.y, xp, yp);
                            if (nodes(xp, yp) == null) {
                                nodes(xp, yp, new Node((short) xp, (short) yp));
                            }
                            Node neighbour = nodes(xp, yp);
                            map.pathFinderVisited(plane, xp, yp);

                            // if the new cost we've determined for this node is lower than

                            // it has been previously makes sure the node hasn'e've
                            // determined that there might have been a better path to getAll to

                            // this node so it needs to be re-evaluated

                            if (nextStepCost < neighbour.cost) {
                                if (inOpenList(neighbour)) {
                                    removeFromOpen(neighbour);
                                }
                                if (inClosedList(neighbour)) {
                                    removeFromClosed(neighbour);
                                }
                            }

                            // if the node hasn't already been processed and discarded then

                            // reset it's cost to our current cost and add it as a next possible

                            // step (i.e. to the open list)

                            if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                                neighbour.cost = nextStepCost;
                                neighbour.heuristic = getHeuristicCost(xp, yp, tx, ty);
                                maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                                addToOpen(neighbour);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // since we'e've processData out of search
        // there was no path. Just return null
        if (tx == sx && ty == sy) {
            nodes(tx, ty).parent = nodes(sx, sy);
        }

        if (nodes(tx, ty).parent == null) {
            indexedNodeMap.clear();
            return null;
        }

        // At this point we've definitely found a path so we can uses the parent

        // references of the nodes to find out way from the target location back

        // to the start recording the nodes on the way.

        Path path = new Path(ctx);
        if (map instanceof RSRegion) {
            path = new Path(true, ctx);
        }

        Node target = nodes(tx, ty);
        while (target != nodes(sx, sy)) {
            path.prependStep(target.x, target.y);
            target = target.parent;
        }
        path.prependStep(sx, sy);

        // deallocate all the bullshit
        indexedNodeMap.clear();

        // thats it, we have our path
        closed.clear();
        open.clear();
        //System.gc();
        return path;
    }

    /**
     * Get the first element from the open list. This is the next
     * one to be searched.
     *
     * @return The first element in the open list
     */
    protected Node getFirstInOpen() {
        return (Node) open.first();
    }

    /**
     * Add a node to the open list
     *
     * @param node The node to be added to the open list
     */
    protected void addToOpen(Node node) {
        open.add(node);
    }

    /**
     * Check if a node is in the open list
     *
     * @param node The node to check for
     * @return True if the node given is in the open list
     */
    protected boolean inOpenList(Node node) {
        return open.contains(node);
    }

    /**
     * Remove a node from the open list
     *
     * @param node The node to remove from the open list
     */
    protected void removeFromOpen(Node node) {
        open.remove(node);
    }

    /**
     * Add a node to the closed list
     *
     * @param node The node to add to the closed list
     */
    protected void addToClosed(Node node) {
        closed.add(node);
    }

    /**
     * Check if the node supplied is in the closed list
     *
     * @param node The node to search for
     * @return True if the node specified is in the closed list
     */
    protected boolean inClosedList(Node node) {
        return closed.contains(node);
    }

    /**
     * Remove a node from the closed list
     *
     * @param node The node to remove from the closed list
     */
    protected void removeFromClosed(Node node) {
        closed.remove(node);
    }

    /**
     * Check if a given location is valid for the supplied mover
     *
     * @param sx The starting x coordinate
     * @param sy The starting y coordinate
     * @param x  The x coordinate of the location to check
     * @param y  The y coordinate of the location to check
     * @return True if the location is valid for the given mover
     */
    protected boolean isValidLocation(int plane, int sx, int sy, int x, int y, int direction) {
        boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles());
        return !(map.getBlock(plane, x, y) == -128 || map.solid(plane, x, y) || invalid) && map.isWalkable(plane, sx, sy, x, y);
    }

    /**
     * Get the cost to move through a given location
     *
     * @param sx The x coordinate of the tile whose cost is being determined
     * @param sy The y coordiante of the tile whose cost is being determined
     * @param tx The x coordinate of the target location
     * @param ty The y coordinate of the target location
     * @return The cost of movement through the given tile
     */
    public float getMovementCost(int plane, int sx, int sy, int tx, int ty) {
        return map.getCost(plane, sx, sy, tx, ty);
    }

    /**
     * Get the heuristic cost for the given location. This determines in which
     * order the locations are processed.
     *
     * @param x  The x coordinate of the tile whose cost is being determined
     * @param y  The y coordiante of the tile whose cost is being determined
     * @param tx The x coordinate of the target location
     * @param ty The y coordinate of the target location
     * @return The heuristic cost assigned to the tile
     */
    public float getHeuristicCost(int x, int y, int tx, int ty) {
        return heuristic.getCost(map, x, y, tx, ty);
    }

    public Node nodes(int x, int y) {
        return indexedNodeMap.get(hash(x, y));
    }

    private Node nodes(int sx, int sy, Node node) {
        indexedNodeMap.put(hash(sx, sy), node);
        return node;
    }


    public static long hash(int a, int b) {
        long a1 = (long) (a >= 0 ? 2 * (long) a : -2 * (long) a - 1);
        long b1 = (long) (b >= 0 ? 2 * (long) b : -2 * (long) b - 1);
        long c1 = (long) ((a1 >= b1 ? a1 * a1 + a1 + b1 : a1 + b1 * b1) / 2);
        return a < 0 && b < 0 || a >= 0 && b >= 0 ? c1 : -c1 - 1;
    }


}
