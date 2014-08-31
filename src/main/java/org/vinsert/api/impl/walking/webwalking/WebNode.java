package org.vinsert.api.impl.walking.webwalking;

import java.util.ArrayList;

import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.Tile;

/**
 * The class used for storing information used by the Djikstra's algorithm with relation to finding the shortest StaticWebNode path
 * @author Cheddy
 *
 */
public class WebNode implements Comparable<WebNode>{
	
	StaticWebNode node;
	private StaticWebConnection previous;
	MethodContext ctx;
	double cost;
	WebNode previousNode;

	public WebNode(MethodContext ctx, StaticWebNode node, StaticWebConnection previousConn, WebNode previousNode) {
		this.ctx = ctx;
		this.node = node;
		this.previous = previousConn;
		this.previousNode = previousNode;
		if(previousConn == null && previousNode == null){
			cost = 0;
		}else if(previousConn == null){
			cost = previousNode.cost;
		}else if(previousConn != null && previousNode != null){
			cost = previousConn.getEuclideanDistance() + previousNode.cost;
		}
	}

	public void addNeighbours(ArrayList<WebNode> openList, ArrayList<WebNode> closedList) {
		for(StaticWebConnection n : node.getLinks()){
			WebNode wn = new WebNode(ctx, n.next, n, this);
			if(listNotContain(wn, openList, closedList)){
				openList.add(wn);
			}
		}
	}

	private static boolean listNotContain(WebNode node, final ArrayList<WebNode> arrayOpen, final ArrayList<WebNode> arrayClosed) {
		for (WebNode n : arrayOpen) {
			if (n.getTile().equals(node.getTile())) {
				return false;
			}
		}
		for (WebNode n : arrayClosed) {
			if (n.getTile().equals(node.getTile())) {
				return false;
			}
		}
		return true;
	}

	public Tile getTile() {
		return node.getTile();
	}

	public int[] getConnections() {
		return node.getConnections();
	}

	public StaticWebConnection getPrevious() {
		return previous;
	}

	public void setPrevious(StaticWebConnection previous) {
		this.previous = previous;
	}

	public MethodContext getCtx() {
		return ctx;
	}

	public void setCtx(MethodContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public int compareTo(WebNode other) {
		double diff = this.cost - other.cost;
		if(diff < 0){
			return -1;
		}else if(diff > 0){
			return 1;
		}else{
			return 0;
		}
	}

}
