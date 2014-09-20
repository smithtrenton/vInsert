package org.vinsert.api.impl.walking.dijkstras;

import java.awt.Point;
import java.util.ArrayList;

import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.Tile;

/**
 * A class to store the 3D Dijkstra node information
 * 
 * @author Cheddy
 *
 */
public class DijkstraNode implements Comparable<DijkstraNode> {

	int g, x, y, z, flags;
	DijkstraNode parent;
	int[][] map;
	MethodContext ctx;
	RSLocalMap sceneMap;
	boolean useUp = false;
	boolean useDown = false;

	public DijkstraNode(MethodContext ctx, DijkstraNode parent, int x, int y, int z, RSLocalMap sceneMap) {
		this.parent = parent;
		this.ctx = ctx;
		this.x = x;
		this.y = y;
		this.z = z;
		this.flags = sceneMap.getFlags(z)[x][y];
		if (parent != null) {
			if (parent.x != x && parent.y != y) {
				this.g = parent.g + 14;
			} else {
				this.g = parent.g + 10;
			}
		} else {
			this.g = 0;
		}
		this.map = sceneMap.getFlags(z);
		this.sceneMap = sceneMap;
	}

	public Point getPoint() {
		return new Point(x, y);
	}

	@Override
	public int compareTo(DijkstraNode o) {
		return this.g - o.g;
	}

	public ArrayList<DijkstraNode> getNeighbours(ArrayList<DijkstraNode> arrayOpen, final ArrayList<DijkstraNode> arrayClosed) {
		if (x - 1 > -1) {
			if (DijkstraFinder.isNotBlockedWest(flags, map[x - 1][y]) || sceneMap.boundaries.containsKey(new Tile((x - 1) + ctx.client.getBaseX(), (y) + ctx.client.getBaseY(), z))) {
				DijkstraNode n = new DijkstraNode(ctx, this, x - 1, y, z, sceneMap);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			} else {
				arrayOpen = addUpAndDownLocations(arrayOpen, arrayClosed, x - 1, y);
			}
		}
		if (x + 1 < map.length) {
			if (DijkstraFinder.isNotBlockedEast(flags, map[x + 1][y]) || sceneMap.boundaries.containsKey(new Tile((x + 1) + ctx.client.getBaseX(), (y) + ctx.client.getBaseY(), z))) {
				DijkstraNode n = new DijkstraNode(ctx, this, x + 1, y, z, sceneMap);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			} else {
				arrayOpen = addUpAndDownLocations(arrayOpen, arrayClosed, x + 1, y);
			}
		}
		if (y - 1 > -1) {
			if (DijkstraFinder.isNotBlockedSouth(flags, map[x][y - 1])
					|| sceneMap.boundaries.containsKey(new Tile((x) + ctx.client.getBaseX(), (y - 1) + ctx.client.getBaseY(), z))) {
				DijkstraNode n = new DijkstraNode(ctx, this, x, y - 1, z, sceneMap);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			} else {
				arrayOpen = addUpAndDownLocations(arrayOpen, arrayClosed, x, y - 1);
			}
		}
		if (y + 1 < map[x].length) {
			if (DijkstraFinder.isNotBlockedNorth(flags, map[x][y + 1])
					|| sceneMap.boundaries.containsKey(new Tile((x) + ctx.client.getBaseX(), (y + 1) + ctx.client.getBaseY(), z))) {
				DijkstraNode n = new DijkstraNode(ctx, this, x, y + 1, z, sceneMap);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			} else {
				arrayOpen = addUpAndDownLocations(arrayOpen, arrayClosed, x, y + 1);
			}

		}

		if (x - 1 > -1 && y - 1 > -1) {
			if (DijkstraFinder.isNotBlockedSouthWest(flags, map[x - 1][y], map[x][y - 1], map[x - 1][y - 1])) {
				DijkstraNode n = new DijkstraNode(ctx, this, x - 1, y - 1, z, sceneMap);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		if (x + 1 < map.length && y + 1 < map[x].length) {
			if (DijkstraFinder.isNotBlockedNorthEast(flags, map[x + 1][y], map[x][y + 1], map[x + 1][y + 1])) {
				DijkstraNode n = new DijkstraNode(ctx, this, x + 1, y + 1, z, sceneMap);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		if (y - 1 > -1 && x + 1 < map.length) {
			if (DijkstraFinder.isNotBlockedSouthEast(flags, map[x + 1][y], map[x][y - 1], map[x + 1][y - 1])) {
				DijkstraNode n = new DijkstraNode(ctx, this, x + 1, y - 1, z, sceneMap);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		if (x - 1 > -1 && y + 1 < map[x].length) {
			if (DijkstraFinder.isNotBlockedNorthWest(flags, map[x - 1][y], map[x][y + 1], map[x - 1][y + 1])) {
				DijkstraNode n = new DijkstraNode(ctx, this, x - 1, y + 1, z, sceneMap);
				if (listNotContain(n, arrayOpen, arrayClosed)) {
					arrayOpen.add(n);
				}
			}
		}
		return arrayOpen;
	}

	public ArrayList<DijkstraNode> addUpAndDownLocations(final ArrayList<DijkstraNode> arrayOpen, ArrayList<DijkstraNode> arrayClosed, int x, int y) {
		DijkstraNode n = new DijkstraNode(ctx, this, x, y, z, sceneMap);
		if (listNotContain(n, arrayOpen, arrayClosed)) {
			if (sceneMap.climbUpLocations.containsKey(new Tile((x) + ctx.client.getBaseX(), (y) + ctx.client.getBaseY(), z)) && z < 2) {
				n.useUp = true;
				DijkstraNode up = new DijkstraNode(ctx, n, x, y, z + 1, sceneMap);
				if (listNotContain(up, arrayOpen, arrayClosed)) {
					if (!arrayClosed.contains(n))
						arrayClosed.add(n);
					arrayOpen.add(up);
				}
			}
			if (sceneMap.climbDownLocations.containsKey(new Tile((x) + ctx.client.getBaseX(), (y) + ctx.client.getBaseY(), z)) && z > 0) {
				n.useDown = true;
				DijkstraNode down = new DijkstraNode(ctx, n, x, y, z - 1, sceneMap);
				if (listNotContain(down, arrayOpen, arrayClosed)) {
					if (!arrayClosed.contains(n))
						arrayClosed.add(n);
					arrayOpen.add(down);
				}
			}
		}
		return arrayOpen;
	}
	
	@Override
	public boolean equals(Object node) {
		if(node instanceof DijkstraNode){
			DijkstraNode dn = (DijkstraNode) node;
			if(this.x == dn.x && this.y == dn.y && this.z == dn.z){
				return true;
			}
		}
		return false;
	}

	public GameObject getBoundaryOnTile() {
		return sceneMap.boundaries.containsKey(getTile()) ? sceneMap.boundaries.get(getTile()) : null;
	}

	public GameObject getUpLocationOnTile() {
		return sceneMap.climbUpLocations.containsKey(getTile()) ? sceneMap.climbUpLocations.get(getTile()) : null;
	}

	public GameObject getDownLocationOnTile() {
		return sceneMap.climbDownLocations.containsKey(getTile()) ? sceneMap.climbDownLocations.get(getTile()) : null;
	}

	public Tile getTile() {
		return new Tile((x) + ctx.client.getBaseX(), (y) + ctx.client.getBaseY(), z);
	}

	private static boolean listNotContain(DijkstraNode node, final ArrayList<DijkstraNode> arrayOpen, final ArrayList<DijkstraNode> arrayClosed) {

		for (DijkstraNode n : arrayOpen) {
			if (n.x == node.x && n.y == node.y && n.z == node.z) {
				return false;
			}
		}
		for (DijkstraNode n : arrayClosed) {
			if (n.x == node.x && n.y == node.y && n.z == node.z) {
				return false;
			}
		}
		return true;
	}

}
