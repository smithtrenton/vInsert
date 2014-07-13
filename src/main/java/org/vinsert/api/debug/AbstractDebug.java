package org.vinsert.api.debug;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.vinsert.api.APIBinding;
import org.vinsert.api.APIModule;
import org.vinsert.api.MethodContext;
import org.vinsert.core.Session;

import java.util.logging.Level;

/**
 * Base class for debuggers.
 */
public abstract class AbstractDebug extends MethodContext {
    private boolean enabled;

    public AbstractDebug(Session session) {
        super.session = session;
        Class<? extends APIModule> api = APIModule.class;
        if (getClass().isAnnotationPresent(APIBinding.class)) {
            api = getClass().getAnnotation(APIBinding.class).value();
            logger.log("Attempting to override default API bindings with " + api.getCanonicalName());
        }

        try {
            APIModule module = api.newInstance();
            module.setContext(this);
            Injector injector = Guice.createInjector(module);
            injector.injectMembers(this);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.log(Level.SEVERE, "Failed to create API bindings", e);
        }
    }

    public abstract String getName();

    public abstract String getShortcode();

    public final boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            super.client = session.getClient();
            session.getEnvironment().getEventBus().register(this, false);
            this.enabled = true;
        } else {
            session.getEnvironment().getEventBus().deregister(this);
            this.enabled = false;
        }
    }

}
