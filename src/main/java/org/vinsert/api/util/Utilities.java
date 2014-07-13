package org.vinsert.api.util;


import org.vinsert.api.collection.StatePredicate;

import java.util.Random;

import static java.lang.Thread.currentThread;

/**
 * A class containing a set of utility methods
 *
 * @author tobiewarburton
 */
public final class Utilities {
    private static final Random random = new Random();

    private Utilities() {
    }

    public static int random(int min, int max) {
        if (min < 0 || max < 0 || Math.abs(max - min) <= 0) {
            return min;
        }
        return random.nextInt(Math.abs(max - min)) + min;
    }

    public static double random(double min, double max) {
        return (min + (Math.random() * max));
    }


    public static long random(long min, long max) {
        return (min + ((long) (Math.random() * max)));
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep was interrupted", e);
        }
    }

    public static void sleep(int min, int max) {
        int ms = random(min, max);
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep was interrupted", e);
        }
    }

    /**
     * Sleeps until the passed predicate returns true or sleeping for longer than the timeout.
     *
     * @param predicate predicate
     * @param timeOut   millis time out
     */
    public static boolean sleepUntil(StatePredicate predicate, long timeOut) {
        Timer timer = new Timer(timeOut);
        boolean success = !predicate.apply();
        while (success && !currentThread().isInterrupted() && timer.isRunning()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted", e);
            }
            success = !predicate.apply();
        }
        return !success;
    }

    /**
     * Sleeps until the passed predicate returns false.
     *
     * @param predicate predicate
     * @param timeOut   millis time out
     */
    public static boolean sleepWhile(StatePredicate predicate, long timeOut) {
        Timer timer = new Timer(timeOut);
        boolean success = predicate.apply();
        while (success && !currentThread().isInterrupted() && timer.isRunning()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted", e);
            }
            success = predicate.apply();
        }
        return !success;
    }
}
