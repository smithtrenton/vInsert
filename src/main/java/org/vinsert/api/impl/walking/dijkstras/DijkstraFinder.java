package org.vinsert.api.impl.walking.dijkstras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.Filter;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.Tile;

public class DijkstraFinder {

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
	private RSLocalMap sceneMap;

	/**
	 * A class used to find the shortest path between tiles and to find the
	 * closest objects relative to tiles
	 * 
	 * @param ctx
	 */
	public DijkstraFinder(MethodContext ctx) {
		this.ctx = ctx;
		sceneMap = new RSLocalMap(ctx, ctx.client.getMaps());
	}

	public GameObject findClosestObjectOnMyPlane(Filter<GameObject> filter) {
		if (ctx == null || ctx.player == null | ctx.client == null) {
			return null;
		}

		ArrayList<DijkstraNode> openList = new ArrayList<DijkstraNode>();
		ArrayList<DijkstraNode> closedList = new ArrayList<DijkstraNode>();

		openList.add(new DijkstraNode(ctx, null, ctx.player.getX() - ctx.client.getBaseX(), ctx.player.getY() - ctx.client.getBaseY(), ctx.client.getPlane(), sceneMap));
		while (openList.size() > 0) {
			Collections.sort(openList);
			DijkstraNode current = openList.get(0);
			if (current.z == ctx.client.getPlane()) {
				GameObject a;
				if ((a = getObjectAtTile(current.getTile(), filter)) != null) {
					return a;
				} else if ((a = getObjectAtTile(new Tile(current.getPoint().x + 1 + ctx.client.getBaseX(), current.getPoint().y + ctx.client.getBaseY(), ctx.client.getPlane()),
						filter)) != null) {
					return a;
				} else if ((a = getObjectAtTile(new Tile(current.getPoint().x - 1 + ctx.client.getBaseX(), current.getPoint().y + ctx.client.getBaseY(), ctx.client.getPlane()),
						filter)) != null) {
					return a;
				} else if ((a = getObjectAtTile(new Tile(current.getPoint().x + ctx.client.getBaseX(), current.getPoint().y + 1 + ctx.client.getBaseY(), ctx.client.getPlane()),
						filter)) != null) {
					return a;
				} else if ((a = getObjectAtTile(new Tile(current.getPoint().x + ctx.client.getBaseX(), current.getPoint().y - 1 + ctx.client.getBaseY(), ctx.client.getPlane()),
						filter)) != null) {
					return a;
				}
				openList = current.getNeighbours(openList, closedList);
			}
			openList.remove(current);
			closedList.add(current);
		}
		return null;

	}

	private GameObject getObjectAtTile(Tile w, Filter<GameObject> filter) {
		if (ctx.objects == null) {
			return null;
		}
		List<GameObject> objects = ctx.objects.getAll();
		for (GameObject o : objects) {
			if (o.getTile().equals(w)) {
				if (o != null && (filter == null || filter.accept(o))) {
					return o;
				}
			}
		}
		return null;
	}

	public DPath findShortestPath(Tile startTile, Tile finishTile) {
		DPath path = new DPath(ctx);
		sceneMap.clearObjects();
		sceneMap.updateObjects();
		ArrayList<DijkstraNode> openList = new ArrayList<DijkstraNode>();
		ArrayList<DijkstraNode> closedList = new ArrayList<DijkstraNode>();

		openList.add(new DijkstraNode(ctx, null, startTile.getX() - ctx.client.getBaseX(), startTile.getY() - ctx.client.getBaseY(), startTile.getZ(), sceneMap));
		while (openList.size() > 0) {
			Collections.sort(openList);
			DijkstraNode current = openList.get(0);

			if (current.getTile().equals(finishTile)) {
				while (current != null) {
					path.path.add(current.getTile());
					if (current.getBoundaryOnTile() != null) {
						path.boundaries.put(current.getTile(), current.getBoundaryOnTile());
					}
					if (current.getUpLocationOnTile() != null && current.useUp) {
						path.climbUpLocations.put(current.getTile(), current.getUpLocationOnTile());
					}
					if (current.getDownLocationOnTile() != null && current.useDown) {
						path.climbDownLocations.put(current.getTile(), current.getDownLocationOnTile());
					}
					current = current.parent;
				}
				Collections.reverse(path.path);
				return path;
			}

			openList = current.getNeighbours(openList, closedList);
			openList.remove(current);
			closedList.add(current);
		}
		return null;
	}

	public static boolean isWalkable(int flag) {
		return (flag & (NON_WALKABLE)) == 0;
	}

	public static boolean isNotBlockedNorth(int currentFlag, int north) {
		if (!isWalkable(north)) {
			return false;
		}
		return (currentFlag & BLOCKED_NORTH) == 0;
	}

	public static boolean isNotBlockedWest(int currentFlag, int west) {
		if (!isWalkable(west)) {
			return false;
		}
		return (currentFlag & BLOCKED_WEST) == 0;
	}

	public static boolean isNotBlockedEast(int currentFlag, int east) {
		if (!isWalkable(east)) {
			return false;
		}
		return (currentFlag & BLOCKED_EAST) == 0;
	}

	public static boolean isNotBlockedSouth(int currentFlag, int south) {
		if (!isWalkable(south)) {
			return false;
		}
		return (currentFlag & BLOCKED_SOUTH) == 0;
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
