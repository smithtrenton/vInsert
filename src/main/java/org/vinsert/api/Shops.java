package org.vinsert.api;

import org.vinsert.api.collection.Filter;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetItem;

import java.util.List;

/**
 * @author const_
 */
public interface Shops {

    /**
     * Gets the shops widget
     *
     * @return widget
     */
    Widget getWidget();

    /**
     * Close shop Widget.
     *
     * @return true if shop is closed else false.
     */
    boolean close();

    /**
     * Checks if the shop Widget is up and visible.
     *
     * @return true if shop is open else false.
     */
    boolean isOpen();

    /**
     * Gets all the items in the shop.
     *
     * @return list containing all the items in the shop.
     */
    List<WidgetItem> getAll();


    /**
     * Gets all the items in the shop.
     *
     * @param filter - the filter to apply
     * @return list containing all the items in the shop.
     */
    List<WidgetItem> getAll(Filter<WidgetItem> filter);

    /**
     * Checks if the shop contains a certain id
     *
     * @param id - the id to look for
     * @return true if the shop contains an item with id otherwise false
     */
    boolean contains(int id);

    /**
     * Gets an item in the shop for a specified id
     *
     * @param id - the id of the item
     * @return item
     */
    WidgetItem getItem(int id);

    /**
     * Gets an item in the shop that accepts the filter
     *
     * @param filter - the filter that finds the item acceptable
     * @return item
     */
    WidgetItem getItem(Filter<WidgetItem> filter);

    /**
     * Buys the specified amount for each id in the varargs id parameter
     *
     * @param amount - the amount of eh itemm to sell
     * @param ids    - the id(s) of the item(s) to sell
     */
    void buy(int amount, int... ids);

    /**
     * Sells the specified amount for each id in the varargs id parameter
     *
     * @param amount - the amount of eh item to sell
     * @param ids    - the id(s) of the item(s) to sell
     */
    void sell(int amount, int... ids);
}
