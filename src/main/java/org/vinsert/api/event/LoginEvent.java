package org.vinsert.api.event;

/**
 * @author : const_
 */
public final class LoginEvent {

    private long time;

    public LoginEvent(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
