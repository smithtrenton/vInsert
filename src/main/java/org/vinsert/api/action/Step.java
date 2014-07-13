package org.vinsert.api.action;

/**
 *
 */
public interface Step {

    /**
     * Check if this step can be executed.
     *
     * @return true if executable
     */
    boolean validate();

    /**
     * Execute the step and determine the next.
     *
     * @return the next step to execute or null.
     */
    Step execute();

}
