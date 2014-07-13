package org.vinsert.api.script;

import org.vinsert.api.MethodContext;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * A script type that uses a set of actions to do something.
 * It will iterate through all defined actions, and if it finds one that validates
 * it will execute it and sleep for the amount of milliseconds specified in the return value.
 * <p/>
 * If no valid actions are found (none validate) the script will exit. It will also exit if
 * an action returns -1.
 */
public abstract class ActionScript extends IterativeScript {
    private final List<Action> actions = newArrayList();

    @Override
    public final int loop() {
        for (Action action : getActions()) {
            action.client = session.getClient();
            action.session = session;
            if (action.validate()) {
                return action.execute();
            }
        }
        return -1;
    }

    /**
     * Gets all defined actions
     *
     * @return actions
     */
    public final List<Action> getActions() {
        return actions;
    }

    /**
     * Defines a set of actions to use.
     *
     * @param actions actions
     */
    public final void define(Action... actions) {
        for (Action action : actions) {
            getEvents().deregister(action);
            inject(action);
            getEvents().register(action, true);
        }
        this.actions.addAll(newArrayList(actions));
    }

    public abstract static class Action extends MethodContext {

        /**
         * Validates that the action can be executed.
         *
         * @return execute?
         */
        public abstract boolean validate();

        /**
         * Executes the action
         *
         * @return delay
         */
        public abstract int execute();
    }
}
