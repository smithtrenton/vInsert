package org.vinsert.core.event;

/**
 * An event triggered by a pulse from either the Heartbeat thread
 * or the client's internal thread (depending on the offloading settings).
 */
public final class PulseEvent {
    private long time;

    public PulseEvent(long time) {
        this.time = time;
    }

    /**
     * The time at which the pulse was generated.
     *
     * @return pulse time
     */
    public long getTime() {
        return time;
    }
}
