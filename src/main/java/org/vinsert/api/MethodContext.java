package org.vinsert.api;

import com.google.inject.Inject;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.Events;
import org.vinsert.api.impl.LocalPlayer;
import org.vinsert.core.Session;
import org.vinsert.core.event.AppletChangedEvent;
import org.vinsert.game.engine.IClient;

/**
 * A collection class for everything that is made available
 * to the script in terms of methods and object instances.
 */
public class MethodContext {

    public Session session;

    public IClient client;

    @Inject
    public Logger logger;

    @Inject
    public LocalPlayer player;

    @Inject
    public Viewport viewport;

    @Inject
    public Widgets widgets;

    @Inject
    public Players players;

    @Inject
    public Npcs npcs;

    @Inject
    public Objects objects;

    @Inject
    public Loots loot;

    @Inject
    public Menu menu;

    @Inject
    public Worlds worlds;

    @Inject
    public Inventory inventory;

    @Inject
    public Equipment equipment;

    @Inject
    public Skills skills;

    @Inject
    public Tabs tabs;

    @Inject
    public Camera camera;

    @Inject
    public Bank bank;

    @Inject
    public Magic magic;

    @Inject
    public Minimap minimap;

    @Inject
    public Mouse mouse;

    @Inject
    public Keyboard keyboard;

    @Inject
    public Settings settings;

    @Inject
    public Walking walking;

    @Inject
    public Shops shops;

    @Inject
    public Prayers prayers;

    @Inject
    public Screen screen;

    @Inject
    public Composites composites;

    public MethodContext(Session session) {
        this.session = session;
        getEventBus().register(this, false);
    }

    public MethodContext() {

    }

    public boolean isLoggedIn() {
        return client.getLoginIndex() == 30 &&
                widgets.find(378, 6).single() == null;
    }

    public Events getEventBus() {
        return session.getEnvironment().getEventBus();
    }

    @EventHandler
    public void onAppletLoaded(AppletChangedEvent event) {
        client = (IClient) event.getApplet();
    }

}
