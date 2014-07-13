package org.vinsert.core;

import org.apache.log4j.Logger;
import org.vinsert.api.MethodContext;
import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.debug.AutoEnable;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.StateChangeEvent;
import org.vinsert.api.util.Utilities;
import org.vinsert.core.control.BreakManager;
import org.vinsert.core.control.DebugManager;
import org.vinsert.core.control.RandomManager;
import org.vinsert.core.control.ScriptManager;
import org.vinsert.core.event.AppletChangedEvent;
import org.vinsert.core.event.EnvironmentChangedEvent;
import org.vinsert.game.AppletLoader;
import org.vinsert.game.engine.IClient;
import org.vinsert.util.AES;

import java.awt.*;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * State mechanism for a botting session.
 */
public final class Session {
    private static final Map<Integer, Session> HASH_SESSION_MAP = newHashMap();
    private static Logger logger = Logger.getLogger(Session.class);
    private DebugManager debugManager;
    private ScriptManager scriptManager;
    private RandomManager randomManager;
    private BreakManager breakManager;
    private Environment environment;
    private Heartbeat heartbeat;
    private AppletLoader loader;
    private State state;
    private long startTime;
    private boolean render = true;

    /**
     * Constructor
     *
     * @param environment environment to construct session in
     */
    public Session(Environment environment) {
        try {
            startTime = System.currentTimeMillis();
            environment.setSession(this);
            this.environment = environment;
            this.debugManager = new DebugManager(this);
            this.randomManager = new RandomManager(this);
            this.scriptManager = new ScriptManager(this);
            this.heartbeat = new Heartbeat(this);
            this.loader = new AppletLoader(this);
            this.breakManager = new BreakManager(this);
            environment.getEventBus().register(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAliveTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }

    /**
     * Constructor that initializes the session
     * with a new and blank environment.
     */
    public Session() {
        this(new Environment());
    }

    public BreakManager getBreakManager() {
        return breakManager;
    }

    @EventHandler
    public void onEnvironmentChanged(EnvironmentChangedEvent event) {
        logger.info(event.getPropertyName() + " changed from " +
                event.getOldValue() + " to " + event.getNewValue());
        switch (event.getPropertyName()) {
            case "synchronizedClock":
                if (!((boolean) event.getNewValue())) {
                    heartbeat.stop();
                } else {
                    heartbeat.start();
                }
                break;
        }
    }

    public void onAppletLoaded() {
        int appletHash = getAppletLoader().getApplet().hashCode();
        HASH_SESSION_MAP.put(appletHash, this);
        setState(State.STOPPED);
        heartbeat.start();

        environment.getEventBus().submit(new AppletChangedEvent(getAppletLoader().getApplet()));
        getClient().setEventBus(environment.getEventBus());
        getClient().setEnvironment(environment);
        getEnvironment().setRender(true);

        /**
         * A bit of a hack, but yeah..
         */
        for (AbstractDebug debug : debugManager.getDebugs()) {
            if (debug.getClass().getAnnotation(AutoEnable.class) != null) {
                debug.setEnabled(true);
            }
        }
    }

    /**
     * Gets the debug manager, used for enabling and disabling
     * various debugging features of the vInsert client.
     *
     * @return debug manager
     */
    public DebugManager getDebugManager() {
        return debugManager;
    }

    /**
     * Gets the script manager, used for starting and stopping scripts
     * and managing their general state as well as their spawned threads.
     *
     * @return script manager
     */
    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    /**
     * Gets the session environment.
     *
     * @return environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Gets the applet loader
     *
     * @return applet loader
     */
    public AppletLoader getAppletLoader() {
        return loader;
    }

    /**
     * Gets the game client
     *
     * @return client
     */
    public IClient getClient() {
        return (IClient) loader.getApplet();
    }

    /**
     * Gets the current session state
     *
     * @return state
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the session state
     *
     * @param state new state.
     */
    public void setState(State state) {
        this.state = state;
        getEnvironment().getEventBus().submit(new StateChangeEvent(state));
    }

    /**
     * Logs in a selected account if one is available.
     *
     * @param context The context of the login request.
     */
    public void login(MethodContext context) {
        final Rectangle userBox = new Rectangle(398, 278, 129, 12);
        String username = getEnvironment().getAccount().getUsername();
        String password = AES.decrypt(getEnvironment().getAccount().getPassword(),
                AES.getMasterPassword());
        context.mouse.click((int) userBox.getCenterX(),
                (int) userBox.getCenterY(), true);
        Utilities.sleep(700, 1000);
        context.keyboard.type(username, true);
        Utilities.sleep(500, 1200);
        context.keyboard.type(password, true);
    }

    /**
     * Destroys the session
     */
    public void destroy() {
        scriptManager.stop();
        getAppletLoader().getApplet().stop();
        getAppletLoader().getApplet().destroy();
    }

    public RandomManager getRandomManager() {
        return randomManager;
    }

    /**
     * Enum representing possible session states
     */
    public static enum State {
        ACTIVE, PAUSED, STOPPED
    }

    public void toggleAllRender(boolean allowed) {
        if (!allowed && canRender() || scriptManager.getScript() == null) {
            return;
        }
        scriptManager.getScript().session.getEnvironment().setRender(allowed);
    }

    public boolean canRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    /**
     * Retrieves a session by the hashcode of its hosted applet.
     *
     * @param hash applet hash
     * @return session
     */
    public static Session getSession(Integer hash) {
        return HASH_SESSION_MAP.get(hash);
    }
}
