package org.vinsert.api;

import org.vinsert.api.wrappers.Tab;

/**
 * @author tobiewarburton
 */
public interface Tabs {
    /**
     * opens a tab
     *
     * @param tab the tab to open
     * @return true on success else false
     */
    boolean open(Tab tab);

    /**
     * checks if the specified tab is open
     *
     * @param tab the tab to check if it's selected
     * @return true if tab is selected else false
     */
    boolean isOpen(Tab tab);

    /**
     * gets the currently selected tab
     *
     * @return the current selected tab
     */
    Tab getCurrent();

    /**
     * Logs out of the game, sleeps if in combat.
     */
    void logout();
}
