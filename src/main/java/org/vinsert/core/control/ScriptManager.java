package org.vinsert.core.control;

import org.apache.log4j.Logger;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.Events;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.Session;
import org.vinsert.core.event.PulseEvent;
import org.vinsert.core.script.stub.AgnosticStub;
import org.vinsert.gui.controller.MainController;

import static org.vinsert.gui.ControllerManager.get;

/**
 * A script management class.
 */
public final class ScriptManager {
    private static final Logger logger = Logger.getLogger(ScriptManager.class);
    private final Events eventBus;
    private final Session session;
    private AbstractScript script;
    private boolean inRandom = false;

    public ScriptManager(Session session) {
        this.eventBus = session.getEnvironment().getEventBus();
        this.session = session;
        eventBus.register(this, false);
    }

    /**
     * Prepares a script to be ran on the session thread.
     *
     * @param scriptStub The script stub
     */
    public void start(final AgnosticStub<? extends AbstractScript, ScriptManifest> scriptStub) {
        if (script != null) {
            logger.error("A script is already running!");
            return;
        }

        try {
            script = scriptStub.instantiate();
            script.init(session);
            session.setState(Session.State.ACTIVE);
            Thread spinUp = new Thread(new Runnable() {
                @Override
                public void run() {
                    script.onStart();
                }
            });
            get(MainController.class).refresh();
            spinUp.setDaemon(true);
            spinUp.start();
        } catch (Exception e) {
            logger.error("Error while starting script", e);
        }
    }

    /**
     * Stops the currently running script.
     */
    public void stop() {
        if (script == null) {
            logger.error("No script to stop.");
            return;
        }

        try {
            eventBus.deregister(script);
            session.setState(Session.State.STOPPED);
            Thread tearDown = new Thread(new Runnable() {
                @Override
                public void run() {
                    script.onStop();
                    script = null;
                }
            });
            get(MainController.class).refresh();
            tearDown.setDaemon(true);
            tearDown.start();
        } catch (Exception e) {
            logger.error("Error while stopping script", e);
        }
    }

    @EventHandler
    public void pulse(PulseEvent event) {
        try {
            if (isInRandom()) {
                logger.info("We're in a random...");
                return;
            }

            if (session.getState() == Session.State.STOPPED) {
                return;
            } else if (script == null) {
                return;
            } else if (script.isStopping()) {
                stop();
                return;
            }
            script.tick();
        } catch (Exception e) {
            logger.error("Exception occurred during pulse!", e);
        }
    }

    public ScriptManifest getManifest() {
        return script.getManifest();
    }

    public AbstractScript getScript() {
        return script;
    }

    public boolean isInRandom() {
        return inRandom;
    }

    public void setInRandom(boolean inRandom) {
        this.inRandom = inRandom;
    }
}
