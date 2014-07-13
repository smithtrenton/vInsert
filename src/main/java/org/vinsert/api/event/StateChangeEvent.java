package org.vinsert.api.event;

import org.vinsert.core.Session;

/**
 *
 */
public final class StateChangeEvent {
    private Session.State state;

    public StateChangeEvent(Session.State state) {
        this.state = state;
    }

    public Session.State getState() {
        return state;
    }

    public void setState(Session.State state) {
        this.state = state;
    }
}
