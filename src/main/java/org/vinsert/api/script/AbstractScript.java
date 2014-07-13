package org.vinsert.api.script;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.vinsert.api.APIBinding;
import org.vinsert.api.APIModule;
import org.vinsert.api.MethodContext;
import org.vinsert.api.event.Events;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.Session;

import java.util.logging.Level;

/**
 * A base-class for all scripts
 */
public abstract class AbstractScript extends MethodContext {
    private Injector injector;
    private boolean stopping;

    public AbstractScript() {
        Class<? extends APIModule> api = APIModule.class;
        if (getClass().isAnnotationPresent(APIBinding.class)) {
            api = getClass().getAnnotation(APIBinding.class).value();
            logger.log("Attempting to override default API bindings with " + api.getCanonicalName());
        }

        try {
            APIModule module = api.newInstance();
            module.setContext(this);
            injector = Guice.createInjector(module);
            injector.injectMembers(this);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.log(Level.SEVERE, "Failed to create API bindings", e);
        }
    }

    public final void init(Session session) {
        this.session = session;
        this.client = session.getClient();
        getEventBus().register(this, true);
    }

    public abstract void tick();

    /**
     * Called when the script starts
     * You can override this instead
     * of binding to the event, but binding
     * to the event is encouraged.
     */
    public void onStart() {

    }

    /**
     * Called when the script stops
     * You can override this instead
     * of binding to the event, but binding
     * to the event is encouraged.
     */
    public void onStop() {

    }

    /**
     * Requests dependency injection in the specified object.
     *
     * @param object object to inject members into.
     */
    public final void inject(Object object) {
        injector.injectMembers(object);
    }

    /**
     * Requests an instance of an object from the injector.
     *
     * @param clazz class
     * @param <T>   object type
     * @return object
     */
    public final <T> T instantiate(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    /**
     * Registers an object to the event bus
     *
     * @param object - the object to register
     */
    public final void register(Object object) {
        getEvents().register(object, true);
    }

    /**
     * Gets the session event bus
     *
     * @return event bus
     */
    public final Events getEvents() {
        return session.getEnvironment().getEventBus();
    }

    public final boolean isStopping() {
        return stopping;
    }

    public final void setStopping(boolean stopping) {
        this.stopping = stopping;
    }


    public final ScriptManifest getManifest() {
        return getClass().getAnnotation(ScriptManifest.class);
    }
}
