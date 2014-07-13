package org.vinsert.api.script;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.vinsert.api.APIBinding;
import org.vinsert.api.APIModule;
import org.vinsert.api.MethodContext;

import java.util.logging.Level;

/**
 *
 */
public abstract class Delegate extends MethodContext {

    public Delegate() {
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

    /**
     * Handles the state
     *
     * @return delay
     */
    public abstract int handle();

}
