package org.vinsert.api.impl.walking.webwalking;

/**
 * The class used for storing links between StaticWebNodes
 * @author Cheddy
 *
 */
public class StaticWebConnection {

	public StaticWebNode prev;
	public StaticWebNode next;
	public double distance;

	public StaticWebConnection(StaticWebNode node1, StaticWebNode node2, double distance) {
		this.prev = node1;
		this.next = node2;
		this.distance = distance;
	}

	public double getEuclideanDistance() {
		return prev.getTile().distanceTo(next.getTile());
	}

}
