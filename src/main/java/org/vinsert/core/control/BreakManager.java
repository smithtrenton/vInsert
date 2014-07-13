package org.vinsert.core.control;

import org.apache.log4j.Logger;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.util.Utilities;
import org.vinsert.core.Session;
import org.vinsert.core.event.PulseEvent;
import org.vinsert.core.model.BreakCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author const_
 */
public final class BreakManager {
    private static final Logger logger = Logger.getLogger(BreakManager.class);
    private final List<BreakCondition> conditions = new ArrayList<>();
    private final Session session;
    private BreakCondition currentBreak;


    public BreakManager(Session session) {
        this.session = session;
        session.getEnvironment().getEventBus().register(this, false);
    }

    @EventHandler
    public void tick(PulseEvent event) {
        if (session.getState() != Session.State.STOPPED) {
            if (currentBreak != null) {
                if (!currentBreak.validate(session.getScriptManager().getScript())) {
                    session.setState(Session.State.ACTIVE);
                    currentBreak = null;
                } else {
                    currentBreak.execute(session.getScriptManager().getScript());
                }
            } else {
                for (BreakCondition condition : conditions) {
                    if (condition.validate(session.getScriptManager().getScript())) {
                        logger.info("Entering break '" + condition.getDesc() + "'.");
                        if (session.getScriptManager().getScript().player.isInCombat()) {
                            logger.info("Player is in combat.. postponing break.");
                            while (!session.getScriptManager().getScript().player.isIdle()) {
                                Utilities.sleep(50, 100);
                            }
                            logger.info("Player seems to have become idle! Logging out!");
                            session.setState(Session.State.PAUSED);
                            session.getScriptManager().getScript().tabs.logout();
                        }
                        session.setState(Session.State.PAUSED);
                        currentBreak = condition;
                        break;
                    }
                }
            }
        }
    }


    /**
     * Adds a break condition to the list of conditions
     *
     * @param condition condition
     */
    public void addBreakCondition(BreakCondition condition) {
        conditions.add(condition);
        logger.info("Added break '" + condition.getDesc() + "'.");
    }

    /**
     * Removes a break condition from the list of conditions
     *
     * @param condition condition
     */
    public void removeBreakCondition(BreakCondition condition) {
        conditions.remove(condition);
        logger.info("Removed break '" + condition.getDesc() + "'.");
    }

    /**
     * Removes all break conditions
     */
    public void clearBreakConditions() {
        conditions.clear();
        logger.info("Removed breaks.");
    }

    /**
     * Checks if there any breaks in this break manager
     *
     * @return true if conditions > 0
     */
    public boolean hasBreaks() {
        return conditions.size() > 0;
    }
}
