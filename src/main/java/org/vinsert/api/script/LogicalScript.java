package org.vinsert.api.script;

import org.vinsert.api.action.Sequence;

import java.util.logging.Level;

/**
 *
 */
public abstract class LogicalScript extends AbstractScript {

    private final Sequence sequence = new Sequence(this);

    public void tick() {
        if (isLoggedIn()) {
            getSequence().execute();
        } else {
            logger.log(Level.WARNING, "Received tick but we're not logged in.");
        }
    }

    public Sequence getSequence() {
        return sequence;
    }

}
