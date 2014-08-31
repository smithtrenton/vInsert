package org.vinsert.api.impl.walking.astar;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.Tile;

/**
 * A class containing methods for generating paths on the current plane using
 * the A-star algorithm, currently does not support obstacle handling
 * @author Cheddy
 *
 */
public class AStar {

	static final int IMPENETRABLE_NORTH = 0x400;
	static final int IMPENETRABLE_SOUTH = 0x4000;
	static final int IMPENETRABLE_EAST = 0x1000;
	static final int IMPENETRABLE_WEST = 0x10000;

	static final int IMPENETRABLE_NORTHWEST = 0x200;
	static final int IMPENETRABLE_SOUTHWEST = 0x8000;
	static final int IMPENETRABLE_NORTHEAST = 0x800;
	static final int IMPENETRABLE_SOUTHEAST = 0x2000;

	static final int BLOCKED_NORTHWEST = 1;
	static final int BLOCKED_SOUTHWEST = 64;
	static final int BLOCKED_NORTHEAST = 4;
	static final int BLOCKED_SOUTHEAST = 16;

	static final int NON_WALKABLE = 256 | 0x200000 | 0x40000;

	static final int BLOCKED_WEST = 128;
	static final int BLOCKED_EAST = 8;
	static final int BLOCKED_NORTH = 2;
	static final int BLOCKED_SOUTH = 32;

	private MethodContext ctx;

	public AStar(MethodContext ctx) {
		this.ctx = ctx;
	}

	public boolean isReachable(Tile destination, boolean finishOnPoint) {
		return generatePath(destination, finishOnPoint) != null;
	}

	public Tile[] generatePath(Tile destination, boolean finishOnPoint) {
		return generateShortestPath(ctx.player.getTile(), destination, finishOnPoint);
	}

	private Tile[] generateShortestPath(Tile startTile, Tile finishTile, boolean finishOnPoint) {
		if (startTile == null || finishTile == null || ctx == null || ctx.client == null || ctx.player == null) {
			return null;
		}
		Point start = new Point(startTile.getX() - ctx.client.getBaseX(), startTile.getY() - ctx.client.getBaseY());
		Point finish = new Point(finishTile.getX() - ctx.client.getBaseX(), finishTile.getY() - ctx.client.getBaseY());
		int[][] map = ctx.client.getMaps()[ctx.client.getPlane()].getFlags();
		if (map == null) {
			return null;
		}
		if (start.x > map.length || finish.x > map.length || start.y > map[0].length || finish.y > map[0].length || start.x < 0 || start.y < 0 || finish.x < 0 || finish.y < 0) {
			return null;
		}
		if (!isWalkable(map[finish.x][finish.y]) && finishOnPoint) {
			return null;
		}
		ArrayList<AStarNode> openList = new ArrayList<AStarNode>();
		ArrayList<AStarNode> closedList = new ArrayList<AStarNode>();

		openList.add(new AStarNode(null, start.x, start.y, map[start.x][start.y], finish, map, finishOnPoint));
		while (openList.size() > 0) {
			Collections.sort(openList);
			AStarNode current = openList.get(0);
			if (current.x == finish.x && current.y == finish.y || (current.x + 1 == finish.x && current.y == finish.y && !finishOnPoint)
					|| (current.x - 1 == finish.x && current.y == finish.y && !finishOnPoint) || (current.x == finish.x && current.y + 1 == finish.y && !finishOnPoint)
					|| (current.x == finish.x && current.y - 1 == finish.y && !finishOnPoint)) {
				return reverseArray(retracePath(current));
			}
			openList = current.getNeighbours(openList, closedList);
			openList.remove(current);
			closedList.add(current);
		}
		return null;
	}

	private Tile[] retracePath(AStarNode current) {
		int baseX = ctx.client.getBaseX();
		int baseY = ctx.client.getBaseY();
		ArrayList<Tile> points = new ArrayList<Tile>();
		points.add(new Tile(current.x + baseX, current.y + baseY, ctx.client.getPlane()));
		while (current.parent != null) {
			current = current.parent;
			points.add(new Tile(current.x + baseX, current.y + baseY, ctx.client.getPlane()));
		}
		return points.toArray(new Tile[points.size()]);
	}

	private static Tile[] reverseArray(Tile[] arr) {
		if (arr == null) {
			return null;
		}
		for (int i = 0; i < arr.length / 2; i++) {
			Tile temp = arr[i];
			arr[i] = arr[arr.length - i - 1];
			arr[arr.length - i - 1] = temp;
		}
		return arr;
	}

	public static boolean isWalkable(int flag) {
		return (flag & (NON_WALKABLE)) == 0;
	}

	public static boolean isNotBlockedNorth(int flag, int flag2) {
		if (!isWalkable(flag2)) {
			return false;
		}
		return (flag & BLOCKED_NORTH) == 0;
	}

	public static boolean isNotBlockedWest(int flag, int flag2) {
		if (!isWalkable(flag2)) {
			return false;
		}
		return (flag & BLOCKED_WEST) == 0;
	}

	public static boolean isNotBlockedEast(int flag, int flag2) {
		if (!isWalkable(flag2)) {
			return false;
		}
		return (flag & BLOCKED_EAST) == 0;
	}

	public static boolean isNotBlockedSouth(int flag, int flag2) {
		if (!isWalkable(flag2)) {
			return false;
		}
		return (flag & BLOCKED_SOUTH) == 0;
	}

	public static boolean isNotBlockedSouthWest(int northeast, int northwest, int southeast, int southwest) {
		return (((southwest & (NON_WALKABLE)) == 0) && ((northeast & (NON_WALKABLE | BLOCKED_WEST | BLOCKED_SOUTH | BLOCKED_SOUTHWEST)) == 0)
				&& ((northwest & (NON_WALKABLE | BLOCKED_SOUTH)) == 0) && ((southeast & (NON_WALKABLE | BLOCKED_WEST)) == 0));
	}

	public static boolean isNotBlockedSouthEast(int northwest, int northeast, int southwest, int southeast) {
		return (((southeast & (NON_WALKABLE)) == 0) && ((northwest & (NON_WALKABLE | BLOCKED_EAST | BLOCKED_SOUTH | BLOCKED_SOUTHEAST)) == 0)
				&& ((northeast & (NON_WALKABLE | BLOCKED_SOUTH)) == 0) && ((southwest & (NON_WALKABLE | BLOCKED_EAST)) == 0));
	}

	public static boolean isNotBlockedNorthEast(int southwest, int southeast, int northwest, int northeast) {
		return (((northeast & (NON_WALKABLE)) == 0) && ((southwest & (NON_WALKABLE | BLOCKED_EAST | BLOCKED_NORTH | BLOCKED_NORTHEAST)) == 0)
				&& ((southeast & (NON_WALKABLE | BLOCKED_NORTH)) == 0) && ((northwest & (NON_WALKABLE | BLOCKED_EAST)) == 0));
	}

	public static boolean isNotBlockedNorthWest(int southeast, int southwest, int northeast, int northwest) {
		return (((northwest & (NON_WALKABLE)) == 0) && ((southeast & (NON_WALKABLE | BLOCKED_WEST | BLOCKED_NORTH | BLOCKED_NORTHWEST)) == 0)
				&& ((southwest & (NON_WALKABLE | BLOCKED_NORTH)) == 0) && ((northeast & (NON_WALKABLE | BLOCKED_WEST)) == 0));
	}

}
