package org.vinsert.api.random;

import org.vinsert.api.MethodContext;
import org.vinsert.api.util.Utilities;
import org.vinsert.core.Session;

/**
 * Random solver base class
 */
public abstract class RandomSolver extends MethodContext {

    /**
     * Determines whether the random can run.
     *
     * @return true if runnable, false otherwise
     */
    public abstract boolean canRun();

    /**
     * Utility method for resetting all the variables that were set
     * in a particular run of the random.
     */
    public void reset() {

    }

    /**
     * Executes the random solver
     *
     * @return delay until next execution or -1
     */
    public abstract int run();

    public void execute() {
        while (canRun() && session.getState() == Session.State.ACTIVE
                && !Thread.currentThread().isInterrupted()) {
            int sleep = run();
            if (sleep != -1) {
                Utilities.sleep(sleep);
            } else {
                break;
            }
        }
        reset();
    }

}
