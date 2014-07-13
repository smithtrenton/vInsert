package org.vinsert.api.util;

import org.vinsert.api.MethodContext;

/**
 * @author const_
 */
public enum ConnectionState {

    LOGIN(10), CONNECTING(20), INITIATION(25), INGAME(30), UNKNOWN(-1);

    private int id;

    private ConnectionState(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public static ConnectionState getState(MethodContext ctx) {
        for (ConnectionState state : values()) {
            if (state.id == ctx.client.getLoginIndex()) {
                return state;
            }
        }
        return null;
    }
}
