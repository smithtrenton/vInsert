package org.vinsert.api;

import org.vinsert.api.collection.queries.WidgetQuery;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetGroup;
import org.vinsert.api.wrappers.interaction.Result;

import java.util.List;

/**
 * Methods for finding and interacting with widgets.
 */
public interface Widgets {

    /**
     * Gets a widget group
     *
     * @param parent - the id of the group
     * @return widget group
     */
    WidgetGroup getGroup(int parent);

    /**
     * Gets an array of all the NON-NULL widget groups in the client.
     *
     * @return widget groups
     */
    List<WidgetGroup> getGroups();

    /**
     * Gets an array of all the NON-NULL widgets in the client
     *
     * @return widgets
     */
    List<Widget> getAll();

    /**
     * Creates a widget query to use widgets
     *
     * @return query
     */
    WidgetQuery find();

    /**
     * Creates a widget query based on the two most basic
     * attribute of a widget, the parent id and the child id.
     *
     * @param parent parent id
     * @param child  child id
     * @return query
     */
    WidgetQuery find(int parent, int child);

    /**
     * Checks if a widget with the continue text is valid
     *
     * @return <t>true if it finds a widget with the continue text</t> otherwise false
     */
    boolean canContinue();

    /**
     * Clicks the first widget with the continue text
     *
     * @return result of the action
     */
    Result clickContinue();

}
