package org.vinsert.api.impl.walking.webwalking;

import java.util.ArrayList;

import org.vinsert.api.wrappers.Tile;

/**
 * The class used for storing information on the "web nodes" or vertices
 * @author Cheddy
 *
 */
public class StaticWebNode {

	private Tile tile;
	private int[] connections;
	@SuppressWarnings("unused")
	private int[] distances;
	private StaticWebConnection[] links;
	
	
	public StaticWebNode(Tile t, int... connections) {
		this.tile = t;
		this.connections = connections;
	}
	
	public void generateConnections(){
		ArrayList<StaticWebConnection> con = new ArrayList<StaticWebConnection>();
		for(int n : getConnections()){
			if(StaticWebNodeStore.WEB_NODES.length > n && n > -1){
				con.add(new StaticWebConnection(this, StaticWebNodeStore.WEB_NODES[n], 0 /* distances[n] */));
			}
		}
		links = con.toArray(new StaticWebConnection[con.size()]);
	}

	public Tile getTile() {
		return tile;
	}

	public int[] getConnections() {
		return connections;
	}

	public StaticWebConnection[] getLinks() {
		return links;
	}

	public void setLinks(StaticWebConnection[] links) {
		this.links = links;
	}
	
}
