package org.vinsert.api.random;

import org.vinsert.api.MethodContext;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Stateful random event solvers.
 */
public abstract class StatefulRandomSolver<T extends Enum> extends RandomSolver {
    private final Map<T, StateHandler> stateMapping = newHashMap();

    @Override
    public int run() {
        T baseState = determine();
        if (baseState != null && stateMapping.containsKey(baseState)) {
            StateHandler nextStateHandler = stateMapping.get(baseState);
            nextStateHandler.client = session.getClient();
            nextStateHandler.session = session;
            return nextStateHandler.handle();
        }
        return -1;
    }

    /**
     * Defines a state handler for specified state.
     *
     * @param state state
     * @param impl  handler
     */
    public void define(T state, StateHandler impl) {
        stateMapping.put(state, impl);
    }

    /**
     * Retrieves a state handler for specified state.
     *
     * @param state state
     * @return state handler
     */
    public StateHandler get(T state) {
        return stateMapping.get(state);
    }

    /**
     * Determines the state of the script.
     *
     * @return current state
     */
    public abstract T determine();

    public abstract class StateHandler extends MethodContext {

        /**
         * Handles the state
         *
         * @return delay
         */
        public abstract int handle();
    }
}
