package org.vinsert.api.impl.walking.astar;

import java.awt.Point;
import java.util.ArrayList;

/**
 * The class used to store information used in the A-star path finder
 * @author Cheddy
 *
 */
public class AStarNode implements Comparable<AStarNode> {

	int f, g, h, x, y, flags;
	AStarNode parent;
	Point finish;
	int[][] map;
	boolean fop;

	public AStarNode(AStarNode parent, int x, int y, int flags, Point finish, int[][] map, boolean finishOnPoint) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.flags = flags;
		if (parent != null) {
			if (parent.x != x && parent.y != y) {
				this.g = parent.g + 14;
			} else {
				this.g = parent.g + 10;
			}

		} else {
			this.g = 0;
		}
		this.h = (Math.abs(x - finish.x) + Math.abs(y - finish.y));
		this.f = h + g;
		this.finish = finish;
		this.map = map;
		this.flags = map[x][y];
		this.fop = finishOnPoint;
	}

	@Override
	public int compareTo(AStarNode o) {
		return this.f - o.f;
	}

	public final ArrayList<AStarNode> getNeighbours(final ArrayList<AStarNode> arrayOpen, final ArrayList<AStarNode> arrayClosed) {
		if (x - 1 > -1) {
			if (AStar.isNotBlockedWest(flags, map[x - 1][y])) {
				AStarNode n = new AStarNode(this, x - 1, y, map[x - 1][y], finish, map, fop);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		if (x + 1 < map.length) {
			if (AStar.isNotBlockedEast(flags, map[x + 1][y])) {
				AStarNode n = new AStarNode(this, x + 1, y, map[x + 1][y], finish, map, fop);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		if (y - 1 > -1) {
			if (AStar.isNotBlockedSouth(flags, map[x][y - 1])) {
				AStarNode n = new AStarNode(this, x, y - 1, map[x][y - 1], finish, map, fop);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		if (y + 1 < map[x].length) {
			if (AStar.isNotBlockedNorth(flags, map[x][y + 1])) {
				AStarNode n = new AStarNode(this, x, y + 1, map[x][y + 1], finish, map, fop);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}

		}

		if (x - 1 > -1 && y - 1 > -1) {
			if (AStar.isNotBlockedSouthWest(flags, map[x - 1][y], map[x][y - 1], map[x - 1][y - 1])) {
				AStarNode n = new AStarNode(this, x - 1, y - 1, map[x - 1][y - 1], finish, map, fop);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		if (x + 1 < map.length && y + 1 < map[x].length) {
			if (AStar.isNotBlockedNorthEast(flags, map[x + 1][y], map[x][y + 1], map[x + 1][y + 1])) {
				AStarNode n = new AStarNode(this, x + 1, y + 1, map[x + 1][y + 1], finish, map, fop);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		if (y - 1 > -1 && x + 1 < map.length) {
			if (AStar.isNotBlockedSouthEast(flags, map[x + 1][y], map[x][y - 1], map[x + 1][y - 1])) {
				AStarNode n = new AStarNode(this, x + 1, y - 1, map[x + 1][y - 1], finish, map, fop);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		if (x - 1 > -1 && y + 1 < map[x].length) {
			if (AStar.isNotBlockedNorthWest(flags, map[x - 1][y], map[x][y + 1], map[x - 1][y + 1])) {
				AStarNode n = new AStarNode(this, x - 1, y + 1, map[x - 1][y + 1], finish, map, fop);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		return arrayOpen;
	}

	private static boolean listNotContain(AStarNode node, final ArrayList<AStarNode> arrayOpen, final ArrayList<AStarNode> arrayClosed) {
		for (AStarNode n : arrayOpen) {
			if (n.x == node.x && n.y == node.y) {
				return false;
			}
		}
		for (AStarNode n : arrayClosed) {
			if (n.x == node.x && n.y == node.y) {
				return false;
			}
		}
		return true;
	}

}
