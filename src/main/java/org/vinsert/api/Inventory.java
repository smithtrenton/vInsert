package org.vinsert.api;

import org.vinsert.api.collection.queries.InventoryQuery;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetItem;

import java.util.List;

/**
 * User: Cov
 * Date: 23/09/13
 * Time: 20:44
 */
public interface Inventory {

    /**
     * Gets the inventory widget
     *
     * @return widget
     */
    Widget getWidget();

    /**
     * Retrieves all items in your inventory
     *
     * @return An array of all items (can be empty).
     */
    List<WidgetItem> getAll();

    /**
     * Constructs a query
     *
     * @return query
     */
    InventoryQuery find();

    /**
     * Constructs a query using the most basic
     * attribute of an inventory item, the ID.
     *
     * @param ids A varargs of item ids
     * @return query
     */
    InventoryQuery find(int... ids);

    /**
     * Constructs a query using an
     * attribute of an inventory item, the name.
     *
     * @param names A varargs of item names
     * @return query
     */
    InventoryQuery find(String... names);

    /**
     * Checks if the inventory contains a range of ids
     *
     * @param ids A varargs array of IDs that are accepted.
     * @return <t>true if the inventory contains an id</t> otherwise false
     */
    boolean contains(int... ids);

    /**
     * Checks if the inventory contains a range of names
     *
     * @param names A varargs array of names that are accepted.
     * @return <t>true if the inventory contains an name</t> otherwise false
     */
    boolean contains(String... names);

    /**
     * Gets the count of a specified id.
     *
     * @param stacks <t>true to count stacks</t>
     * @param id     the id to look for
     * @return the count
     */
    int getCount(boolean stacks, int id);

    /**
     * Gets the count of a specified name.
     *
     * @param stacks <t>true to count stacks</t>
     * @param name   the name to look for
     * @return the count
     */
    int getCount(boolean stacks, String name);

    /**
     * Drops all items matching the specified IDs
     *
     * @param ids set of items to drop
     */
    void dropAll(int... ids);

    /**
     * Drops all items matching the specified names
     *
     * @param names set of items to drop
     */
    void dropAll(String... names);

    /**
     * Drops all items matching the specified IDs
     *
     * @param ids set of items to drop
     */
    void dropAllExcept(int... ids);

    /**
     * Drops all items matching the specified names
     *
     * @param names set of items to drop
     */
    void dropAllExcept(String... names);

    /**
     * Drops all items in the inventory.
     */
    void dropAll();

    /**
     * Drops one item of specified ID.
     *
     * @param id item id
     */
    void drop(int id);

    /**
     * Drops one item of specified name.
     *
     * @param name item name
     */
    void drop(String name);

    /**
     * Gets the count of the inventory
     *
     * @return the size (int)
     */
    int getCount();

    /**
     * Checks if the inventory is full
     *
     * @return <t>true if the inventory has 28 items</t> otherwise false
     */
    boolean isFull();

    /**
     * Checks if the inventory is empty
     *
     * @return <t>true if the inventory has 0 items</t> otherwise false
     */
    boolean isEmpty();
}
