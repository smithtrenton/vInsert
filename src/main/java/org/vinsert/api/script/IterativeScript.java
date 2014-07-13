package org.vinsert.api.script;

import org.apache.log4j.Logger;

/**
 * A base-class for iterative scripts.
 * Iterative scripts are scripts that progress
 * through the game using a logic loop.
 */
public abstract class IterativeScript extends LogicalScript {
    private static Logger logger = Logger.getLogger(IterativeScript.class);
    private long lastTick;
    private long tickDelay;

    public abstract int loop();

    /**
     * Simulates a game tick to which the script should react.
     */
    public void tick() {
        if (!isLoggedIn()) {
            logger.debug("Received tick but not logged in...");
            return;
        } else if ((System.currentTimeMillis() - lastTick) < tickDelay) {
            long remaining = tickDelay - (System.currentTimeMillis() - lastTick);
            logger.debug("Awaiting tick delay.. executing in " + remaining);
            return;
        }

        int delay = loop();
        if (delay > -1) {
            tickDelay = delay;
            lastTick = System.currentTimeMillis();
        }
    }
}
