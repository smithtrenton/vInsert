package org.vinsert.api.impl.walking.dijkstras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.Filter;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.Tile;

public class DijkstraFinder {

	static final int BLOCKED_NORTHWEST = 1 | 0x200;
	static final int BLOCKED_SOUTHWEST = 64 | 0x8000;
	static final int BLOCKED_NORTHEAST = 4 | 0x800;
	static final int BLOCKED_SOUTHEAST = 16 | 0x2000;

	static final int NON_WALKABLE = 256 | 0x200000 | 0x40000;

	static final int BLOCKED_WEST = 128 | 0x10000;
	static final int BLOCKED_EAST = 8 | 0x1000;
	static final int BLOCKED_NORTH = 2 | 0x400;
	static final int BLOCKED_SOUTH = 32 | 0x4000;

	private MethodContext ctx;
	private RSLocalMap sceneMap;

	/**
	 * A class used to find the shortest path between tiles and to find the closest objects relative to tiles
	 * @param ctx
	 */
	public DijkstraFinder(MethodContext ctx) {
		this.ctx = ctx;
		sceneMap = new RSLocalMap(ctx, ctx.client.getMaps());
	}

	public GameObject[] findClosestObjectsOnMyPlane(Filter<GameObject> filter) {
		ArrayList<GameObject> objects = new ArrayList<GameObject>();
		if(ctx == null || ctx.player == null | ctx.client == null){
			return null;
		}

		ArrayList<DijkstraNode> openList = new ArrayList<DijkstraNode>();
		ArrayList<DijkstraNode> closedList = new ArrayList<DijkstraNode>();

		openList.add(new DijkstraNode(ctx, null, ctx.player.getX() - ctx.client.getBaseX(), ctx.player.getY() - ctx.client.getBaseY(), ctx.client.getPlane(), sceneMap));
		while (openList.size() > 0) {
			Collections.sort(openList);
			DijkstraNode current = openList.get(0);
			if (current.z == ctx.client.getPlane()) {
				ArrayList<GameObject> a;
				if ((a = getObjectsAtTile(current.getTile(), filter)) != null) {
					if (!objects.contains(a))
						objects.addAll(a);
				} else if ((a = getObjectsAtTile(new Tile(current.getPoint().x + 1 + ctx.client.getBaseX(), current.getPoint().y + ctx.client.getBaseY(), ctx.client.getPlane()),
						filter)) != null) {
					if (!objects.contains(a))
						objects.addAll(a);
				} else if ((a = getObjectsAtTile(new Tile(current.getPoint().x - 1 + ctx.client.getBaseX(), current.getPoint().y + ctx.client.getBaseY(), ctx.client.getPlane()),
						filter)) != null) {
					if (!objects.contains(a))
						objects.addAll(a);
				} else if ((a = getObjectsAtTile(new Tile(current.getPoint().x + ctx.client.getBaseX(), current.getPoint().y + 1 + ctx.client.getBaseY(), ctx.client.getPlane()),
						filter)) != null) {
					if (!objects.contains(a))
						objects.addAll(a);
				} else if ((a = getObjectsAtTile(new Tile(current.getPoint().x + ctx.client.getBaseX(), current.getPoint().y - 1 + ctx.client.getBaseY(), ctx.client.getPlane()),
						filter)) != null) {
					if (!objects.contains(a))
						objects.addAll(a);
				}
				openList = current.getNeighbours(openList, closedList);
			}
			openList.remove(current);
			closedList.add(current);
		}
		return objects.toArray(new GameObject[objects.size()]);

	}

	private ArrayList<GameObject> getObjectsAtTile(Tile w, Filter<GameObject> filter) {
		if(ctx.objects == null){
			return new ArrayList<GameObject>();
		}
		List<GameObject> objects = ctx.objects.getAll();
		ArrayList<GameObject> obs = new ArrayList<GameObject>();
		for (GameObject o : objects) {
			if (o.getTile().equals(w)) {
				if (filter == null || filter.accept(o)) {
					obs.add(o);
				}
			}
		}
		return obs;
	}

	public DPath findShortestPath(Tile startTile, Tile finishTile) {
		DPath path = new DPath(ctx);

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
