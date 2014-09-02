package org.vinsert.api.impl.walking.dijkstras;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.util.RandomFunctions;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.Tile;
import org.vinsert.api.wrappers.interaction.Result;

/**
 * The class used to store paths which include object handling
 * 
 * @author Cheddy
 *
 */
public class DPath {

	/**
	 * The path to be traversed
	 */
	public ArrayList<Tile> path = new ArrayList<Tile>();

	/**
	 * A map containing the locations and objects of all the boundaries to be
	 * passed on this path
	 */
	public HashMap<Tile, GameObject> boundaries = new HashMap<Tile, GameObject>();

	/**
	 * A map containing the locations and objects of all the "climbable" objects
	 * to be climbed up on this path
	 */
	public HashMap<Tile, GameObject> climbUpLocations = new HashMap<Tile, GameObject>();

	/**
	 * A map containing the locations and objects of all the "climbable" objects
	 * to be climbed down on this path
	 */
	public HashMap<Tile, GameObject> climbDownLocations = new HashMap<Tile, GameObject>();

	MethodContext ctx;

	private int i = 0;

	public DPath(MethodContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * A function used to reverse the order of any tile array
	 * 
	 * @param tileArray
	 * @return The reversed tile array
	 */
	public static Tile[] reverseArray(Tile[] tileArray) {
		if (tileArray == null) {
			return null;
		}
		for (int i = 0; i < tileArray.length / 2; i++) {
			Tile temp = tileArray[i];
			tileArray[i] = tileArray[tileArray.length - i - 1];
			tileArray[tileArray.length - i - 1] = temp;
		}
		return tileArray;
	}

	@Override
	public String toString() {
		return path.toString();
	}

	/**
	 * A function which when invoked will attempt to traverse your character
	 * along the given DPath
	 * 
	 * @return Whether or not traversal of the path was successful
	 */
	public boolean traverse() {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int counterIndices = 0;
		int counter = 0;
		indices.add(0);
		for (int i = 0; i < path.size(); i++) {
			if (boundaries.containsKey(path.get(i)) || climbUpLocations.containsKey(path.get(i)) || climbDownLocations.containsKey(path.get(i))) {
				indices.add(i);
			}
		}
		indices.add(path.size());

		while (!ctx.player.getTile().equals(path.get(path.size() - 1)) && counterIndices < indices.size()) {

			int fails = 0;
			while (!ctx.player.getTile().equals(path.get((indices.get(counterIndices + 1) - 1)))) {
				int oldCounter = counter;
				for (i = (indices.get(counterIndices + 1) - 1); i > counter; i--) {
					Point tilePoint = ctx.viewport.convert(path.get(i));
					if (ctx.viewport.isInViewport(tilePoint)) {
						ctx.mouse.move(tilePoint);
						if (ctx.menu.getHoverAction().equals("Walk here")) {
							ctx.mouse.click(true);
							counter = i;
							if (!Utilities.sleepUntil(new StatePredicate() {

								@Override
								public boolean apply() {
									return ctx != null && ctx.player != null && path.size() > i && ctx.player.getTile().distanceTo(path.get(i)) < 5;
								}
							}, 10000)) {
								return false;
							}
						} else {
							Result res = ctx.menu.click("Walk here");
							if (res == Result.OK) {
								counter = i;
								if (!Utilities.sleepUntil(new StatePredicate() {

									@Override
									public boolean apply() {
										return ctx != null && ctx.player != null && path.size() > i && ctx.player.getTile().distanceTo(path.get(i)) < 5;
									}
								}, 10000)) {
									return false;
								}
							} else {
								fails++;
							}
						}
					}
				}
				if (oldCounter == counter) {
					fails++;
				}
				if (fails >= 3) {
					return false;
				}
			}
			if (boundaries.containsKey(path.get((indices.get(counterIndices + 1))))) {
				GameObject ob = boundaries.get(path.get((indices.get(counterIndices + 1))));
				if (ob.isValid() && ob.isOnScreen() && ob.isInMenu("Open").apply()) {
					Result res = Result.MISSED;
					int fails1 = 0;
					while (res != Result.OK) {
						res = ob.interact("Open");
						if (res == Result.NOT_IN_MENU || res == Result.NOT_ON_SCREEN) {
							return false;
						} else if (res == Result.MISSED) {
							fails1++;
						}
						if (fails1 >= 3) {
							return false;
						}
					}
					try {
						Thread.sleep(RandomFunctions.random(1000, 1500));
					} catch (InterruptedException e) {
						e.printStackTrace();
						return false;
					}
				} else if (ob.isValid() && ob.isOnScreen() && !ob.isInMenu("Open").apply()) {
					continue;
				} else {
					return false;
				}
			} else if (climbUpLocations.containsKey(path.get((indices.get(counterIndices + 1))))) {
				GameObject ob = climbUpLocations.get(path.get((indices.get(counterIndices + 1))));
				if (ob.isValid() && ob.isOnScreen() && ob.isInMenu("Climb-up").apply()) {
					Result res = Result.MISSED;
					int fails1 = 0;
					while (res != Result.OK) {
						res = ob.interact("Climb-up");
						if (res == Result.NOT_IN_MENU || res == Result.NOT_ON_SCREEN) {
							return false;
						} else if (res == Result.MISSED) {
							fails1++;
						}
						if (fails1 >= 3) {
							return false;
						}
					}
					try {
						Thread.sleep(RandomFunctions.random(2000, 3500));
					} catch (InterruptedException e) {
						e.printStackTrace();
						return false;
					}
				} else {
					return false;
				}
			} else if (climbDownLocations.containsKey(path.get((indices.get(counterIndices + 1))))) {
				GameObject ob = climbDownLocations.get(path.get((indices.get(counterIndices + 1))));
				if (ob.isValid() && ob.isOnScreen() && ob.isInMenu("Climb-down").apply()) {
					Result res = Result.MISSED;
					int fails1 = 0;
					while (res != Result.OK) {
						res = ob.interact("Climb-down");
						if (res == Result.NOT_IN_MENU || res == Result.NOT_ON_SCREEN) {
							return false;
						} else if (res == Result.MISSED) {
							fails1++;
						}
						if (fails1 >= 3) {
							return false;
						}
					}
					try {
						Thread.sleep(RandomFunctions.random(2000, 3500));
					} catch (InterruptedException e) {
						e.printStackTrace();
						return false;
					}
				} else {
					return false;
				}
			}
			counterIndices++;

		}

		return true;
	}

}
