package org.vinsert.api.action.impl;

import org.vinsert.api.action.Step;
import org.vinsert.api.random.Validated;

/**
 * A step that delays the execution for a set amount of milliseconds, after
 * which it will continue with the next step (if there is one).
 */
@Validated
public final class Delay implements Step {
    private final int milliseconds;
    private final Step next;

    public Delay(int milliseconds, Step next) {
        this.milliseconds = milliseconds;
        this.next = next;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Step execute() {
        try {
            Thread.sleep(milliseconds);
            return next;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public static Delay get(int milliseconds, Step next) {
        return new Delay(milliseconds, next);
    }

    public static Delay get(int milliseconds) {
        return get(milliseconds, null);
    }
}
