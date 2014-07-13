package org.vinsert.api.impl.tabs;

import com.google.inject.Inject;
import org.vinsert.api.Inventory;
import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.queries.InventoryQuery;
import org.vinsert.api.wrappers.Tab;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetItem;
import org.vinsert.core.Session;

import java.awt.*;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: Cov
 * Date: 23/09/13
 * Time: 20:49
 */
public final class InventoryImpl implements Inventory {
    private final MethodContext ctx;
    private static final int GROUP_ID = 149;
    private static final int CHILD_ID = 0;

    @Inject
    public InventoryImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.util.List<WidgetItem> getAll() {
        java.util.List<WidgetItem> items = newArrayList();
        Widget inventory = getWidget();
        if (inventory != null) {
            int[] ids = inventory.getInventory();
            int[] stacks = inventory.getInventoryStackSizes();
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] > 0 && stacks[i] > 0) {
                    items.add(new WidgetItem(ctx, new Rectangle((inventory.getX() + ((i % 4) * 42)),
                            (inventory.getY() + ((i / 4) * 36)), 31, 31), ids[i] - 1, stacks[i],
                            inventory, i, WidgetItem.Type.INVENTORY
                    ));
                }
            }
        }
        return items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        int size = 0;
        for (int id : getWidget().getInventory()) {
            if (id > 0) {
                size++;
            }
        }
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(int... ids) {
        for (WidgetItem item : getAll()) {
            for (int id : ids) {
                if (item.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(String... names) {
        return find(names).exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount(boolean stacks, int id) {
        int count = 0;
        if (contains(id)) {
            for (WidgetItem item : getAll()) {
                if (item.getId() == id) {
                    count += (stacks ? item.getStackSize() : 1);
                }
            }
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount(boolean stacks, String name) {
        int count = 0;
        for (WidgetItem item : find(name).asList()) {
            count += (stacks ? item.getStackSize() : 1);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dropAll() {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        Collection<WidgetItem> items = getAll();
        for (WidgetItem item : items) {
            if (ctx.session.getState() != Session.State.ACTIVE) {
                break;
            } else if (contains(item.getId())) {
                item.interact("Drop");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dropAll(int... ids) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        java.util.List<WidgetItem> items = find(ids).asList();
        for (WidgetItem item : items) {
            if (ctx.session.getState() != Session.State.ACTIVE) {
                break;
            } else if (contains(item.getId())) {
                item.interact("Drop");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dropAll(String... names) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        java.util.List<WidgetItem> items = find(names).asList();
        for (WidgetItem item : items) {
            if (ctx.session.getState() != Session.State.ACTIVE) {
                break;
            } else {
                item.interact("Drop");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dropAllExcept(int... ids) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        for (WidgetItem item : getAll()) {
            if (ctx.session.getState() != Session.State.ACTIVE) {
                continue;
            }

            boolean validate = true;
            for (int id : ids) {
                if (item.getId() == id) {
                    validate = false;
                }
            }

            if (validate && contains(item.getId())) {
                item.interact("Drop");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dropAllExcept(String... names) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        items:
        for (WidgetItem item : getAll()) {
            if (ctx.session.getState() != Session.State.ACTIVE) {
                continue;
            }

            for (String name : names) {
                if (item.getComposite() != null &&
                        item.getComposite().getName() != null &&
                        item.getComposite().getName().equals(name)) {
                    continue items;
                }
            }
            item.interact("Drop");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drop(int id) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        WidgetItem item = find().id(id).single();
        if (item != null) {
            item.interact("Drop");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drop(String name) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        WidgetItem item = find(name).single();
        if (item != null) {
            item.interact("Drop");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFull() {
        return getCount() == 28;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getWidget() {
        return ctx.widgets.find(GROUP_ID, CHILD_ID).single();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryQuery find() {
        return new InventoryQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryQuery find(int... ids) {
        return find().id(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryQuery find(String... names) {
        return find().named(names);
    }
}
