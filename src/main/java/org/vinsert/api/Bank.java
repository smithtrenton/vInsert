package org.vinsert.api;

import org.vinsert.api.collection.queries.BankQuery;
import org.vinsert.api.impl.tabs.BankImpl;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetItem;

import java.util.List;

/**
 * @author const_
 */
public interface Bank {

    /**
     * Gets the amount of items in the bank
     *
     * @return amt of items in bank
     */
    int getSize();

    /**
     * Checks whether the bank is open or not.
     *
     * @return return true if bank interface is up else false.
     */
    boolean isOpen();

    /**
     * Opens the nearest on screen bank, with priority for objects over npcs.
     *
     * @return true if bank interface is showing.
     */
    boolean open();

    /**
     * Closes the bank interface using the close widget.
     *
     * @return true if bank interface isn't showing.
     */
    boolean close();

    /**
     * Retrieves all the items in the Bank
     *
     * @return an array containing all items in the Bank.
     */
    List<WidgetItem> getAll();

    /**
     * Checks if the Bank contains a specific item
     *
     * @param ids WidgetItem ids to look for
     * @return true if found, otherwise false.
     */
    boolean contains(int... ids);

    /**
     * Checks if the Bank contains all of the specified items
     *
     * @param ids A var-args list of ids.
     * @return true if all the items were found, false otherwise.
     */
    boolean containsAll(int... ids);

    BankQuery find(int... ids);

    BankQuery find(String... names);

    /**
     * Counts all the items matching the specified ids.
     *
     * @param ids    WidgetItem ids of the items to count
     * @param stacks <t>true to count stacks</t>
     * @return total amount of items ? stacks matching id in Bank.
     */
    int count(boolean stacks, int... ids);

    /**
     * Withdraws all of the items matching the specified ID (or as much as the inventory can hold.)
     *
     * @param id The ID of the item to withdraw
     * @return true if inventory contains item after withdrawing.
     */
    boolean withdrawAll(int id);

    /**
     * Withdraws all of the items matching the specified name (or as much as the inventory can hold.)
     *
     * @param name The name of the item to withdraw
     * @return true if inventory contains item after withdrawing.
     */
    boolean withdrawAll(String name);

    /**
     * Withdraws one item matching specified ID
     *
     * @param id ID of item to withdraw
     * @return true if inventory contains item after withdrawing.
     */
    boolean withdrawOne(int id);

    /**
     * Withdraws one item from the bank by name
     *
     * @param name name of the item
     * @return true if inventory contains item after withdrawing
     */
    boolean withdrawOne(String name);

    /**
     * Withdraw X amount of item matching specified ID
     *
     * @param id     ID of item to withdraw
     * @param amount Quantity of item to withdraw
     * @return true if inventory contains item after withdrawing.
     */
    boolean withdrawX(int id, int amount);

    /**
     * Withdraw X amount of item matching specified name
     *
     * @param name   name of the item to withdraw
     * @param amount quantity of the item to withdraw
     * @return true if inventory contains item after withdrawing
     */
    boolean withdrawX(String name, int amount);

    /**
     * Deposits all items of which the id matches any of the IDs specified.
     *
     * @param amount the amount of each item to deposit
     * @param ids    the ids to deposit
     * @return <t>true if successful</t>
     */
    boolean deposit(int amount, int... ids);

    /**
     * Deposits items in inventory bar certain ids
     * items of which the id doesn't match any of the IDs specified.
     *
     * @param amount the amount of each item to deposit
     * @param ids    the ids not to deposit
     * @return <t>true if successful</t>
     */
    boolean depositExcept(int amount, int... ids);

    /**
     * Deposits the inventory using the despot inventory button
     *
     * @return <t>true if inventory is empty</t> otherwise false
     */
    boolean depositAll();

    /**
     * Deposits items in inventory bar certain ids
     * items of which the id doesn't match any of the IDs specified.
     *
     * @param ids the ids not to deposit
     * @return <t>true if successful</t>
     */
    boolean depositAllExcept(int... ids);

    /**
     * Constructs a bank query
     *
     * @return query
     */
    BankQuery find();


    /**
     * Checks if the Bank contains a specific item
     *
     * @param names WidgetItem names to look for
     * @return true if found, otherwise false.
     */
    boolean contains(String... names);

    /**
     * Checks if the Bank contains all of the specified items
     *
     * @param names A var-args list of names.
     * @return true if all the items were found, false otherwise.
     */
    boolean containsAll(String... names);

    /**
     * Counts all the items matching the specified names.
     *
     * @param names  WidgetItem names of the items to count
     * @param stacks <t>true to count stacks</t>
     * @return total amount of items ? stacks matching id in Bank.
     */
    int count(boolean stacks, String... names);

    boolean depositAllOf(String name);

    boolean depositOneOf(String name);

    boolean depositXOf(String name, int amount);

    boolean depositAllOf(int id);

    boolean depositOneOf(int id);

    boolean depositXOf(int id, int amount);

    /**
     * Deposits all items of which the id matches any of the names specified.
     *
     * @param amount the amount of each item to deposit
     * @param names  the names to deposit
     * @return <t>true if successful</t>
     */
    boolean deposit(int amount, String... names);

    /**
     * Deposits items in inventory bar certain names
     * items of which the id doesn't match any of the names specified.
     *
     * @param amount the amount of each item to deposit
     * @param names  the names not to deposit
     * @return <t>true if successful</t>
     */
    boolean depositExcept(int amount, String... names);

    /**
     * Deposits items in inventory bar certain names
     * items of which the id doesn't match any of the namess specified.
     *
     * @param names the ids not to deposit
     * @return <t>true if successful</t>
     */
    boolean depositAllExcept(String... names);

    /**
     * Deposits the inventory using the button
     *
     * @return <t>true if the inventory was deposited</t>
     */
    boolean depositInventory();

    /**
     * Deposits equipment using buttons
     *
     * @return <t>true if the equipment was deposited</t>
     */
    boolean depositEquipment();

    /**
     * Gets the bank widget
     *
     * @return widget
     */
    Widget getWidget();

    /**
     * Opens a bank tab
     *
     * @param tab the bank tab to open
     * @return <t>true if the tab is opened</t> otherwise false
     */
    boolean openTab(BankImpl.Tab tab);

    /**
     * Gets the amount of tabs, 0 if none
     *
     * @return tab count
     */
    int getTabCount();

    /**
     * Gets the current open bank tab
     *
     * @return bank tab
     */
    BankImpl.Tab getCurrent();

    /**
     * Creates a bank tab with an item with the given name
     *
     * @param name the name of the item
     * @return <t>true if the tab was created</t> otherwise false
     */
    boolean createTab(String name);

    /**
     * Creates a bank tab with an item with the given id
     *
     * @param id the id of the item
     * @return <t>true if the tab was created</t> otherwise false
     */
    boolean createTab(int id);

    /**
     * Creates a bank tab with an item
     *
     * @param item the item
     * @return <t>true if the tab was created</t> otherwise false
     */
    boolean createTab(WidgetItem item);

    /**
     * Checks if a specified tab is the current tab
     *
     * @param tab the tab to check if is open
     * @return <t>true if the tab is open</t> otherwise false
     */
    boolean isOpen(BankImpl.Tab tab);
}
