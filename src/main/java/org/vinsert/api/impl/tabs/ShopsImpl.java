package org.vinsert.api.impl.tabs;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Shops;
import org.vinsert.api.collection.Filter;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : const_
 */
public final class ShopsImpl implements Shops {

    private static final int WIDGET_GROUP = 300;
    private static final int ITEMS_PANE = 75;
    private static final int SHOP_CLOSE_ID = 92;
    private final MethodContext ctx;

    @Inject
    public ShopsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean close() {
        if (isOpen()) {
            ctx.widgets.find(WIDGET_GROUP, SHOP_CLOSE_ID).single().click(true);
            Utilities.sleepUntil(new StatePredicate() {
                @Override
                public boolean apply() {
                    return !isOpen();
                }
            }, 1000);
        }
        return !isOpen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return ctx.widgets.find(WIDGET_GROUP, SHOP_CLOSE_ID).single() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WidgetItem> getAll() {
        Widget widget = getWidget();
        if (widget != null) {
            List<WidgetItem> items = new ArrayList<>(widget.getInventory().length);
            for (int i = 0; i < widget.getInventory().length; i++) {
                if (widget.getInventory()[i] - 1 > 0 && widget.getInventoryStackSizes()[i] > 0) {
                    int col = (i % 8);
                    int row = (i / 8);
                    int x = widget.getX() + (col * 47) + 22;
                    int y = widget.getY() + (row * 47) + 18;
                    Rectangle area = new Rectangle(x - (46 / 2), y - (36 / 2), 32, 32);
                    WidgetItem item = new WidgetItem(ctx, area, widget.getInventory()[i] - 1, widget.getInventoryStackSizes()[i],
                            widget, i, WidgetItem.Type.SHOP);
                    items.add(item);
                }
            }
            return items;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WidgetItem> getAll(Filter<WidgetItem> filter) {
        Widget widget = getWidget();
        if (widget != null) {
            List<WidgetItem> items = new ArrayList<>(widget.getInventory().length);
            for (int i = 0; i < widget.getInventory().length; i++) {
                if (widget.getInventory()[i] - 1 > 0 && widget.getInventoryStackSizes()[i] > 0) {
                    int col = (i % 8);
                    int row = (i / 8);
                    int x = widget.getX() + (col * 47) + 22;
                    int y = widget.getY() + (row * 47) + 18;
                    Rectangle area = new Rectangle(x - (46 / 2), y - (36 / 2), 32, 32);
                    WidgetItem item = new WidgetItem(ctx, area, widget.getInventory()[i] - 1, widget.getInventoryStackSizes()[i],
                            widget, i, WidgetItem.Type.SHOP);
                    if (filter.accept(item)) {
                        items.add(item);
                    }
                }
            }
            return items;
        }
        return null;
    }

    public Widget getWidget() {
        return ctx.widgets.find(WIDGET_GROUP, ITEMS_PANE).single();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(int id) {
        if (!isOpen()) {
            return false;
        }
        for (WidgetItem item : getAll()) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetItem getItem(int id) {
        if (!isOpen()) {
            return null;
        }
        for (WidgetItem item : getAll()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetItem getItem(Filter<WidgetItem> filter) {
        if (!isOpen()) {
            return null;
        }
        for (WidgetItem item : getAll()) {
            if (filter.accept(item)) {
                return item;
            }
        }
        return null;
    }

    private void trade(int amount, WidgetItem item, boolean sell) {
        if (!isOpen()) {
            return;
        }
        switch (amount) {
            case 1:
                item.interact((sell ? "Sell" : "Buy") + " 1");
                break;
            case 5:
                item.interact((sell ? "Sell" : "Buy") + " 5");
                break;
            case 10:
                item.interact((sell ? "Sell" : "Buy") + " 10");
                break;
            default:
                item.interact((sell ? "Sell" : "Buy") + " 10");//I suppose just incase scripters are mentally challenged?
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void buy(int amount, int... ids) {
        if (!isOpen()) {
            return;
        }
        for (int id : ids) {
            if (!contains(id)) {
                continue;
            }
            WidgetItem item = getItem(id);
            trade(amount, item, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sell(int amount, int... ids) {
        if (!isOpen()) {
            return;
        }
        for (int id : ids) {
            if (!ctx.inventory.contains(id)) {
                continue;
            }
            WidgetItem item = ctx.inventory.find().id(id).single();
            trade(amount, item, true);
        }
    }
}
