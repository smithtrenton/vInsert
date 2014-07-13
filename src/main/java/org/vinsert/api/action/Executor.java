package org.vinsert.api.action;

import org.apache.log4j.Logger;
import org.vinsert.api.action.meta.Parallel;
import org.vinsert.api.random.Validated;


/**
 *
 */
public final class Executor implements Runnable {
    private final Logger logger = Logger.getLogger(Executor.class);
    private final Thread self = new Thread(this);
    private final Sequence owner;
    private final Step step;
    private boolean async;

    Executor(Sequence owner, Step step, boolean async) {
        this.owner = owner;
        this.step = step;
        this.async = async;
    }

    public void execute() {
        try {
            if (async) {
                self.start();
            } else {
                run();
            }
        } catch (Exception e) {
            logger.error("Invoking step '" + step.getClass().getSimpleName() + "' failed!", e);
        }
    }

    @Override
    public void run() {
        Executor next = from(owner, step.execute());
        if (next != null) {
            next.execute();
        }
    }

    public static Executor from(Sequence owner, Step step) {
        if (step != null && step.validate()) {
            Class<?> stepClass = step.getClass();
            if ((isValidated(stepClass))) {
                return new Executor(owner, step, isParallel(stepClass));
            }
        }
        return null;
    }

    private static boolean isParallel(Class<?> stepClass) {
        return stepClass.isAnnotationPresent(Parallel.class);
    }

    private static boolean isValidated(Class<?> stepClass) {
        return stepClass.isAnnotationPresent(Validated.class);
    }
}
