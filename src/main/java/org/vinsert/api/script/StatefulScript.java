package org.vinsert.api.script;


import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * A state based script.
 */
public abstract class StatefulScript<T extends Enum> extends IterativeScript {
    private final Map<T, Delegate> stateMapping = newHashMap();

    @Override
    public final int loop() {
        T baseState = determine();
        if (baseState != null && stateMapping.containsKey(baseState)) {
            Delegate nextDelegate = stateMapping.get(baseState);
            nextDelegate.client = session.getClient();
            nextDelegate.session = session;
            return nextDelegate.handle();
        }
        return -1;
    }

    /**
     * Defines a state handler for specified state.
     *
     * @param state state
     * @param impl  handler
     */
    public void define(T state, Delegate impl) {
        getEvents().deregister(impl);
        getEvents().register(impl, true);
        inject(impl);
        stateMapping.put(state, impl);
    }

    /**
     * Retrieves a state handler for specified state.
     *
     * @param state state
     * @return state handler
     */
    public Delegate get(T state) {
        return stateMapping.get(state);
    }

    /**
     * Determines the state of the script.
     *
     * @return current state
     */
    public abstract T determine();

}
