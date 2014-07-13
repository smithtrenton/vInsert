package org.vinsert.core.event;

import java.applet.Applet;

/**
 * An event fired whenever the applet of a session is changed.
 */
public final class AppletChangedEvent {
    private final Applet applet;

    public AppletChangedEvent(Applet applet) {
        this.applet = applet;
    }

    public Applet getApplet() {
        return applet;
    }
}
