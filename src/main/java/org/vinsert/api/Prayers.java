package org.vinsert.api;

import org.vinsert.api.wrappers.Prayer;

import java.util.List;

/**
 * Access to prayers
 *
 * @author tommo
 */
public interface Prayers {

    /**
     * Returns the player's current prayer points
     *
     * @return The amount of prayer points
     */
    int getPrayerPoints();

    /**
     * Returns a list of all currently activated prayers
     *
     * @return The list of active prayers
     */
    List<Prayer> getActivatedPrayers();

    /**
     * Checks if the player has a specified prayer activated
     *
     * @param prayer The prayer to check
     * @return <b>true</b> if the prayer is activated, <b>false</b> if not
     */
    boolean isActivated(Prayer prayer);

    /**
     * Checks if player has the required prayer level to activate a specified prayer.
     *
     * @param prayer The prayer to check
     * @return <b>true</b> if the player <i>can</i> activate the prayer, <b>false</b> if <i>not</i>
     */
    boolean canActivate(Prayer prayer);

    /**
     * Attempts to activate a specified prayer
     *
     * @param prayer The prayer to activate
     * @return <b>true</b> if the prayer has been activated, or if the prayer is already activate, and
     * <b>false</b> if something went wrong; such as not having the required level
     */
    boolean activate(Prayer prayer);

    /**
     * Attempts to deactivate a specified prayer
     *
     * @param prayer The prayer to deactivate
     * @return <b>true</b> if the prayer has been deactivated, or if the prayer is already deactivated, and
     * <b>false</b> if something went wrong
     */
    boolean deactivate(Prayer prayer);

}
