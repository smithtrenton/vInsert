package org.vinsert.api.impl.walking.webwalking;

import java.util.ArrayList;
import java.util.Collections;

import org.vinsert.api.MethodContext;
import org.vinsert.api.Walking.Direction;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.impl.walking.astar.AStar;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Tile;

/**
 * The class containing a web-walker based off Djikstra's algorithm
 * 
 * @author Cheddy
 *
 */
public class DjikstraWebWalker {

	MethodContext ctx;

	enum WalkingResult {
		NO_PATH, NULL_TILE, FAILED_WALKING, SUCCESS
	};

	public DjikstraWebWalker(MethodContext ctx) {
		this.ctx = ctx;
	}

	public WalkingResult walk(Tile finish, int range) {
		Tile start = ctx.player.getTile();
		if (start == null || finish == null) {
			return WalkingResult.NULL_TILE;
		}
		StaticWebNode[] path = calculateShortestNodePath(start, finish);
		if (path == null) {
			return WalkingResult.NO_PATH;
		}

		AStar astar = new AStar(ctx);
		if (path.length > 1) {
			for (StaticWebNode node : path) {
				Tile[] localPath = astar.generatePath(node.getTile(), true);
				if (localPath == null) {
					return WalkingResult.NO_PATH;
				}
				ctx.walking.traverse(localPath, Direction.FORWARDS);
			}
		}
		Tile[] localPath = astar.generatePath(finish, true);
		if (localPath == null) {
			return WalkingResult.NO_PATH;
		}
		ctx.walking.traverse(localPath, Direction.FORWARDS);
		Utilities.sleepUntil(new StatePredicate() {

			@Override
			public boolean apply() {
				return !ctx.player.isMoving();
			}
		}, 15000);
		if (finish.distanceTo(ctx.player.getTile()) <= range) {
			return WalkingResult.SUCCESS;
		}

		return WalkingResult.FAILED_WALKING;
	}

	private StaticWebNode[] calculateShortestNodePath(Tile start, Tile finish) {
		if (start == null || finish == null) {
			return null;
		}
		StaticWebNode startNode = nearestNodeTo(start);
		StaticWebNode finishNode = nearestNodeTo(finish);
		if (startNode.equals(finishNode)) {
			return new StaticWebNode[] {
				startNode
			};
		}
		WebNode startt = new WebNode(ctx, startNode, null, null);
		ArrayList<WebNode> openList = new ArrayList<WebNode>();
		ArrayList<WebNode> closedList = new ArrayList<WebNode>();
		openList.add(startt);

		while (openList.size() > 0) {
			Collections.sort(openList);
			WebNode current = openList.get(0);
			if (current.node.equals(finishNode)) {
				return reverseArray(retracePath(current));
			}
			current.addNeighbours(openList, closedList);
			closedList.add(current);
			openList.remove(current);
		}
		return null;
	}

	private StaticWebNode[] retracePath(WebNode current) {
		ArrayList<StaticWebNode> nodes = new ArrayList<StaticWebNode>();
		while (current != null) {
			nodes.add(current.node);
			current = current.previousNode;
		}
		return nodes.toArray(new StaticWebNode[nodes.size()]);
	}

	private static StaticWebNode[] reverseArray(StaticWebNode[] arr) {
		if (arr == null) {
			return null;
		}
		for (int i = 0; i < arr.length / 2; i++) {
			StaticWebNode temp = arr[i];
			arr[i] = arr[arr.length - i - 1];
			arr[arr.length - i - 1] = temp;
		}
		return arr;
	}

	private StaticWebNode nearestNodeTo(Tile tile) {
		StaticWebNode nearest = null;
		double dist = Double.MAX_VALUE;
		for (StaticWebNode i : StaticWebNodeStore.WEB_NODES) {
			if (nearest == null || i.getTile().distanceTo(tile) < dist) {
				nearest = i;
				dist = i.getTile().distanceTo(tile);
			}
		}
		return nearest;
	}

}
