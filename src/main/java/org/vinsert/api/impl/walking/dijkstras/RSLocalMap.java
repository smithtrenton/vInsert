package org.vinsert.api.impl.walking.dijkstras;

import java.util.HashMap;
import java.util.List;

import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.GameObject.GameObjectType;
import org.vinsert.api.wrappers.Tile;
import org.vinsert.game.engine.scene.ICollisionMap;

/**
 * A class used to store the region's collision maps, boundaries, and climbable objects
 * @author Cheddy
 *
 */
public class RSLocalMap {

	private ICollisionMap[] collisionMapAccessors;
	private MethodContext ctx;
	HashMap<Tile, GameObject> boundaries = new HashMap<Tile, GameObject>();
	HashMap<Tile, GameObject> climbUpLocations = new HashMap<Tile, GameObject>();
	HashMap<Tile, GameObject> climbDownLocations = new HashMap<Tile, GameObject>();

	public RSLocalMap(MethodContext ctx, ICollisionMap[] collisionMapAccessors) {
		this.collisionMapAccessors = collisionMapAccessors;
		this.ctx = ctx;
	}

	public int[][] getMyPlaneFlags() {
		if (collisionMapAccessors != null && ctx.client.getPlane() > -1 && ctx.client.getPlane() < collisionMapAccessors.length) {
			return collisionMapAccessors[ctx.client.getPlane()].getFlags();
		}
		return null;
	}

	public int[][] getFlags(int plane) {
		if (collisionMapAccessors != null && plane > -1 && plane < collisionMapAccessors.length) {
			return collisionMapAccessors[plane].getFlags();
		}
		return null;
	}

	private static boolean arrayContains(Object object, Object... array) {
		if (object != null && array != null) {
			for (Object o : (Object[]) array) {
				if (o.equals(object)) {
					return true;
				}
			}
		}
		return false;
	}

	public void updateObjects() {
		if (isValid()) {
			List<GameObject> objects = ctx.objects.getAll();
			for (GameObject o : objects) {
				if (o.getType() == GameObjectType.BOUNDARY) {
					boundaries.put(o.getTile(), o);
				} else if (arrayContains("Climb-up", (Object[]) o.getComposite().getActions())) {
					climbUpLocations.put(o.getTile(), o);
				} else if (arrayContains("Climb-down", (Object[]) o.getComposite().getActions())) {
					climbDownLocations.put(o.getTile(), o);
				}
			}
		}
	}

	public boolean isValid() {
		if (ctx != null && collisionMapAccessors != null && ctx.client != null && ctx.client.getPlane() > -1 && ctx.client.getPlane() < collisionMapAccessors.length) {
			return true;
		}
		return false;
	}

	public void clearObjects() {
		boundaries.clear();
		climbUpLocations.clear();
		climbDownLocations.clear();
	}

}
