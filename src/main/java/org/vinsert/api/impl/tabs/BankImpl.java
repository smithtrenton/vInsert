package org.vinsert.api.impl.tabs;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.vinsert.api.Bank;
import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.collection.queries.BankQuery;
import org.vinsert.api.collection.queries.InventoryQuery;
import org.vinsert.api.collection.queries.WidgetQuery;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetItem;
import org.vinsert.api.wrappers.interaction.Result;
import org.vinsert.api.wrappers.interaction.SceneNode;
import org.vinsert.core.Session;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author const_
 */
public final class BankImpl implements Bank {

    private static final int NEED_UP_LESS = 84;
    private static final int NEED_DOWN_GREATER = 260;
    private static final int BANK_ID = 12;
    private static final int BANK_PANE_ID = 10;
    private static final int BANK_SCROLL_ID = 11;
    private static final int WITHDRAW_X_PARENT = 548;
    private static final int WITHDRAW_X_CHILD = 122;
    private static final int TABS_ID = 8;
    private static final int TITLE_ID = 2;
    private static final int SIZE_ID = 3;
    private static final int DEPOSIT_INVENTORY_ID = 25;
    private static final int DEPOSIT_EQUIPMENT_ID = 27;
    private static final int SCROLL_UP_ID = 4;
    private static final int SCROLL_DOWN_ID = 5;
    private static final int BANK_ACTION_BAR = 1;
    private final MethodContext ctx;
    private static final Logger logger = Logger.getLogger(BankImpl.class);

    @Inject
    public BankImpl(MethodContext ctx) {
        this.ctx = ctx;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getWidget() {
        return ctx.widgets.find().id(BANK_ID, BANK_PANE_ID).single();
    }

    private Widget getBankScrollWidget() {
        return ctx.widgets.find().id(BANK_ID, BANK_SCROLL_ID).single();
    }

    private Widget getTabWidget() {
        return ctx.widgets.find().id(BANK_ID, TABS_ID).single();
    }

    public int getSize() {
        if (isOpen()) {
            Widget widget = ctx.widgets.find(BANK_ID, SIZE_ID).single();
            if (widget != null && widget.getText() != null) {
                return Integer.parseInt(ctx.widgets.find(BANK_ID, SIZE_ID).single().getText());
            }
        }
        return 475;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return getWidget() != null;
    }

    private boolean isVisible(WidgetItem item) {
        return item.getWidget().getY() > NEED_UP_LESS &&
                item.getWidget().getY() < NEED_DOWN_GREATER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean open() {
        if (!isOpen()) {
            SceneNode bank = null;
            if (ctx.objects.find().hasAction("Bank").exists()) {
                bank = ctx.objects.find().hasAction("Bank").single();
            } else if (ctx.npcs.find().hasAction("Bank").exists()) {
                bank = ctx.npcs.find().hasAction("Bank").single();
            } else if (ctx.objects.find("Bank chest").exists()) {
                bank = ctx.objects.find("Bank chest").single();
            } else if (ctx.objects.find("Closed chest").exists()) {
                switch (ctx.objects.find("Closed chest").single().interact("Open")) {
                    case OK:
                        Utilities.sleepUntil(new StatePredicate() {
                            @Override
                            public boolean apply() {
                                return ctx.objects.find().hasAction("Bank").exists();
                            }
                        }, 4000);
                        if (ctx.objects.find().hasAction("Bank").exists()) {
                            bank = ctx.objects.find().hasAction("Bank").single();
                        }
                        break;
                    default:
                        if (ctx.objects.find("Closed chest").exists()) {
                            ctx.walking.walk(ctx.objects.find("Closed chest").single());
                        }
                        break;
                }
            }
            if (bank == null) {
                return isOpen();
            }
            ctx.camera.setAngle(ctx.camera.getAngleTo(bank.getTile()));
            Result result = bank.interact("Bank");
            switch (result) {
                case OK:
                    Utilities.sleepUntil(walking, 5000);
                    Utilities.sleepWhile(closed, 2000);
                    break;

                case NOT_IN_MENU:
                case NOT_ON_SCREEN:
                    ctx.camera.turnTo(bank);
                    break;

                case MISSED:
                    break;
            }
        }
        return isOpen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean close() {
        if (isOpen()) {
            Widget close = ctx.widgets.find().id(BANK_ID, BANK_ACTION_BAR).single();
            if (close != null) {
                close.getChild(11).click(true);
                Utilities.sleepUntil(closed, 2000);
            }
        }
        return !isOpen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.util.List<WidgetItem> getAll() {
        java.util.List<WidgetItem> widgets = newArrayList();
        Widget bank = getWidget();
        int i = 0;
        int max = getSize();
        for (Widget child : bank.getChildren()) {
            if (i > max) {
                break;
            }
            if (child.getItemId() > 0) {
                widgets.add(new WidgetItem(ctx, child.getItemId(),
                        child.getItemStackSize(), child, child.getId(), WidgetItem.Type.BANK));
            }
            i++;
        }
        return widgets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(int... ids) {
        return find().id(ids).asList().size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(int... ids) {
        for (int id : ids) {
            if (!find(id).exists()) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankQuery find(int... ids) {
        return find().id(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankQuery find(String... names) {
        return find().named(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int count(boolean stacks, int... ids) {
        int count = 0;
        for (WidgetItem item : find().id(ids).asList()) {
            count += stacks ? item.getStackSize() : 1;
        }
        return count;
    }

    private void scrollTo(final WidgetItem item) {
        if (isVisible(item) || item.getWidget() == null || item.getWidget().getArea() == null ||
                getBankScrollWidget() == null) {
            return;
        }
        if (item.getWidget().getY() < NEED_UP_LESS) {
            ctx.mouse.move(getBankScrollWidget().getChild(SCROLL_UP_ID).getClickPoint());
        }
        if (item.getWidget().getY() > NEED_DOWN_GREATER) {
            ctx.mouse.move(getBankScrollWidget().getChild(SCROLL_DOWN_ID).getClickPoint());
        }
        ctx.mouse.press(ctx.mouse.getPosition());
        Utilities.sleepUntil(new StatePredicate() {
            @Override
            public boolean apply() {
                return isVisible(item);
            }
        }, 6000);
        ctx.mouse.release(ctx.mouse.getPosition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean withdrawAll(final String name) {
        if (!isOpen()) {
            if (!open()) {
                return false;
            }
        }
        BankQuery query = find().named(name);
        WidgetItem item = query.single();
        if (item == null) {
            return false;
        }
        if (getCurrent() != Tab.ALL && !isVisible(item)) {
            openTab(Tab.ALL);
        }
        final int count = ctx.inventory.getCount(true, name);
        if (ctx.session.getState() == Session.State.ACTIVE) {
            ensureVisibility(item);
            item = query.single();
            switch (item.interact("Withdraw-All")) {
                case OK:
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return ctx.inventory.getCount(true, name) > count;
                        }
                    }, 600);
                    break;
                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    ensureVisibility(item);
                    break;

            }
        }
        return ctx.inventory.getCount(true, name) > count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean withdrawOne(final String name) {
        if (!isOpen()) {
            if (!open()) {
                return false;
            }
        }
        BankQuery query = find().named(name);
        WidgetItem item = query.single();
        if (item == null) {
            return false;
        }
        if (getCurrent() != Tab.ALL && !isVisible(item)) {
            openTab(Tab.ALL);
        }
        final int count = ctx.inventory.getCount(true, name);
        if (ctx.session.getState() == Session.State.ACTIVE) {
            ensureVisibility(item);
            item = query.single();
            switch (item.interact("Withdraw")) {
                case OK:
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return ctx.inventory.getCount(true, name) == count + 1;
                        }
                    }, 800);
                    break;
                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    ensureVisibility(item);
                    break;

            }
        }
        return ctx.inventory.getCount(true, name) == count + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean withdrawX(final String name, final int amount) {
        if (!isOpen()) {
            if (!open()) {
                return false;
            }
        }
        BankQuery query = find().named(name);
        WidgetItem item = query.single();
        if (item == null) {
            return false;
        }
        if (getCurrent() != Tab.ALL && !isVisible(item)) {
            openTab(Tab.ALL);
        }
        final int count = ctx.inventory.getCount(true, name);
        if (ctx.session.getState() == Session.State.ACTIVE) {
            ensureVisibility(item);
            item = query.single();
            switch (item.interact("Withdraw-X")) {
                case OK:
                    final WidgetQuery wxQuery = ctx.widgets.find(WITHDRAW_X_PARENT, WITHDRAW_X_CHILD).visible().text("Enter amount:");
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return wxQuery.single() != null;
                        }
                    }, 2000);
                    if (wxQuery.single() == null) {
                        break;
                    }
                    ctx.keyboard.type(String.valueOf(amount), true);
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return ctx.inventory.getCount(true, name) == count + amount;
                        }
                    }, 600);
                    break;
                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    ensureVisibility(item);
                    break;

            }
        }
        return ctx.inventory.getCount(true, name) == count + amount;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean withdrawAll(final int id) {
        if (!isOpen()) {
            if (!open()) {
                return false;
            }
        }
        BankQuery query = find().id(id);
        WidgetItem item = query.single();
        if (item == null) {
            return false;
        }
        if (getCurrent() != Tab.ALL && !isVisible(item)) {
            openTab(Tab.ALL);
        }
        final int count = ctx.inventory.getCount(true, id);
        if (ctx.session.getState() == Session.State.ACTIVE) {
            ensureVisibility(item);
            item = query.single();
            switch (item.interact("Withdraw-All")) {
                case OK:
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return ctx.inventory.getCount(true, id) > count;
                        }
                    }, 600);
                    break;
                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    ensureVisibility(item);
                    break;

            }
        }
        return ctx.inventory.getCount(true, id) > count;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean withdrawOne(final int id) {
        if (!isOpen()) {
            if (!open()) {
                return false;
            }
        }
        BankQuery query = find().id(id);
        WidgetItem item = query.single();
        if (item == null) {
            return false;
        }
        if (getCurrent() != Tab.ALL && !isVisible(item)) {
            openTab(Tab.ALL);
        }
        final int count = ctx.inventory.getCount(true, id);
        if (query.exists() && ctx.session.getState() == Session.State.ACTIVE) {
            ensureVisibility(item);
            item = query.single();
            switch (item.interact("Withdraw")) {
                case OK:
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return ctx.inventory.getCount(true, id) == count + 1;
                        }
                    }, 600);
                    break;
                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    ensureVisibility(item);
                    break;

            }
        }
        return ctx.inventory.getCount(true, id) == count + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean withdrawX(final int id, final int amount) {
        if (!isOpen()) {
            if (!open()) {
                return false;
            }
        }
        BankQuery query = find().id(id);
        WidgetItem item = query.single();
        if (item == null) {
            return false;
        }
        if (getCurrent() != Tab.ALL && !isVisible(item)) {
            openTab(Tab.ALL);
        }
        final int count = ctx.inventory.getCount(true, id);
        if (ctx.session.getState() == Session.State.ACTIVE) {
            ensureVisibility(item);
            item = query.single();
            switch (item.interact("Withdraw-X")) {
                case OK:
                    final WidgetQuery wxQuery = ctx.widgets.find(WITHDRAW_X_PARENT, WITHDRAW_X_CHILD).visible().text("Enter amount:");
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return wxQuery.single() != null;
                        }
                    }, 2000);
                    if (wxQuery.single() == null) {
                        break;
                    }
                    ctx.keyboard.type(String.valueOf(amount), true);
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return ctx.inventory.getCount(true, id) > count;
                        }
                    }, 600);
                    break;
                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    ensureVisibility(item);
                    break;

            }
        }
        return ctx.inventory.getCount(true, id) > count;
    }

    private void ensureVisibility(final WidgetItem item) {
        scrollTo(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deposit(int amount, int... ids) {
        if (!isOpen()) {
            open();
        }
        if (!ctx.inventory.contains(ids)) {
            return false;
        }
        java.util.List<WidgetItem> items = ctx.inventory.getAll();
        for (WidgetItem item : items) {
            for (int id : ids) {
                if (item.getId() == id) {
                    switch (amount) {
                        case 0:
                            item.interact("Deposit-All");
                            break;
                        case 1:
                            item.interact("Deposit-1");
                            break;
                        case 5:
                            item.interact("Deposit-5");
                            break;
                        case 10:
                            item.interact("Deposit-10");
                            break;
                        default:
                            if (amount >= item.getStackSize()) {
                                item.interact("Deposit-All");
                            } else if (item.interact("Deposit-X") == Result.OK) {
                                Utilities.sleep(400, 800);
                                ctx.keyboard.type(String.valueOf(amount), true);
                            }
                            break;
                    }
                    Utilities.sleep(300, 700);
                }
            }
        }
        return true;//NEED TO IMPROVE
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositAll() {
        Widget widget = ctx.widgets.find().id(BANK_ID, 26).single();
        if (widget != null) {
            widget.click(true);
            Utilities.sleepUntil(new StatePredicate() {
                @Override
                public boolean apply() {
                    return ctx.inventory.getCount() == 0;
                }
            }, 2000);
        }
        return ctx.inventory.getCount() == 0;
    }

    @Override
    public boolean depositAllExcept(int... ids) {
        for (WidgetItem item : ctx.inventory.getAll()) {
            boolean validate = true;
            for (int id : ids) {
                if (item.getId() == id) {
                    validate = false;
                }
            }

            if (validate && ctx.inventory.contains(item.getId())) {
                item.interact("Deposit-All");
                Utilities.sleep(200, 450);
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositExcept(int amount, int... ids) {
        for (WidgetItem item : ctx.inventory.getAll()) {
            for (int id : ids) {
                if (item.getId() != id) {
                    deposit(0, item.getId());
                    break;
                }
            }
        }
        for (WidgetItem item : ctx.inventory.getAll()) {
            for (int id : ids) {
                if (item.getId() == id) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankQuery find() {
        return new BankQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(String... names) {
        return find().named(names).asList().size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(String... names) {
        for (String name : names) {
            if (!find(name).exists()) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int count(boolean stacks, String... names) {
        int count = 0;
        for (WidgetItem item : find().named(names).asList()) {
            count += stacks ? item.getStackSize() : 1;
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositAllOf(String name) {
        InventoryQuery query = ctx.inventory.find(name);
        if (!query.exists()) {
            return false;
        }

        if (query.exists() && ctx.session.getState() == Session.State.ACTIVE) {
            WidgetItem item = query.single();
            switch (item.interact("deposit-All")) {
                case OK:
                    Utilities.sleep(100, 300);
                    return true;

                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    ensureVisibility(item);
                    depositAllOf(name);
                    break;

            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositOneOf(String name) {
        InventoryQuery query = ctx.inventory.find(name);
        if (!query.exists()) {
            return false;
        }

        if (query.exists() && ctx.session.getState() == Session.State.ACTIVE) {
            WidgetItem item = query.single();
            switch (item.interact("deposit")) {
                case OK:
                    Utilities.sleep(100, 300);
                    return true;

                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    depositOneOf(name);
                    break;

            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositXOf(String name, int amount) {
        InventoryQuery query = ctx.inventory.find(name);
        if (!query.exists()) {
            return false;
        }

        if (query.exists() && ctx.session.getState() == Session.State.ACTIVE) {
            WidgetItem item = query.single();
            switch (item.interact("Deposit-X")) {
                case OK:
                    final WidgetQuery wxQuery = ctx.widgets.find(WITHDRAW_X_PARENT, WITHDRAW_X_CHILD).visible().text("Enter amount:");
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return wxQuery.single() != null;
                        }
                    }, 2000);
                    if (wxQuery.single() == null) {
                        return false;
                    }
                    ctx.keyboard.type(String.valueOf(amount), true);
                    Utilities.sleep(100, 300);
                    return true;

                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    break;

            }
        }
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositAllOf(int id) {
        InventoryQuery query = ctx.inventory.find(id);
        if (!query.exists()) {
            return false;
        }

        if (query.exists() && ctx.session.getState() == Session.State.ACTIVE) {
            WidgetItem item = query.single();
            switch (item.interact("Deposit-All")) {
                case OK:
                    Utilities.sleep(100, 300);
                    return true;

                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    break;

            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositOneOf(int id) {
        InventoryQuery query = ctx.inventory.find(id);
        if (!query.exists()) {
            return false;
        }

        if (query.exists() && ctx.session.getState() == Session.State.ACTIVE) {
            WidgetItem item = query.single();
            switch (item.interact("Deposit")) {
                case OK:
                    Utilities.sleep(100, 300);
                    return true;

                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    break;

            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositXOf(int id, int amount) {
        InventoryQuery query = ctx.inventory.find(id);
        if (!query.exists()) {
            return false;
        }

        if (query.exists() && ctx.session.getState() == Session.State.ACTIVE) {
            WidgetItem item = query.single();
            switch (item.interact("Deposit-X")) {
                case OK:
                    final WidgetQuery wxQuery = ctx.widgets.find(WITHDRAW_X_PARENT, WITHDRAW_X_CHILD).visible().text("Enter amount:");
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return wxQuery.single() != null;
                        }
                    }, 2000);
                    if (wxQuery.single() == null) {
                        return false;
                    }
                    ctx.keyboard.type(String.valueOf(amount), true);
                    Utilities.sleep(100, 300);
                    return true;

                case NOT_ON_SCREEN:
                case NOT_IN_MENU:
                case MISSED:
                    break;

            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deposit(int amount, String... names) {
        if (!isOpen()) {
            open();
        }
        if (!ctx.inventory.contains(names)) {
            return false;
        }
        java.util.List<WidgetItem> items = ctx.inventory.getAll();
        for (WidgetItem item : items) {
            for (String name : names) {
                if (item.getName() != null && item.getName().equalsIgnoreCase(name)) {
                    switch (amount) {
                        case 0:
                            item.interact("Deposit-All");
                            break;
                        case 1:
                            item.interact("Deposit-1");
                            break;
                        case 5:
                            item.interact("Deposit-5");
                            break;
                        case 10:
                            item.interact("Deposit-10");
                            break;
                        default:
                            if (amount >= item.getStackSize()) {
                                item.interact("Deposit-All");
                            } else if (item.interact("Deposit-X") == Result.OK) {
                                Utilities.sleep(400, 800);
                                ctx.keyboard.type(String.valueOf(amount), true);
                            }
                            break;
                    }
                    Utilities.sleep(300, 700);
                }
            }
        }
        return true;//NEED TO IMPROVE
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositExcept(int amount, String... names) {
        for (WidgetItem item : ctx.inventory.getAll()) {
            for (String name : names) {
                if (item.getName() != null && item.getName().equals(name)) {
                    deposit(0, item.getId());
                    break;
                }
            }
        }
        for (WidgetItem item : ctx.inventory.getAll()) {
            for (String name : names) {
                if (item.getName() != null && item.getName().equals(name)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositAllExcept(String... names) {
        for (WidgetItem item : ctx.inventory.getAll()) {
            boolean validate = true;
            for (String name : names) {
                if (item.getName() != null && item.getName().equalsIgnoreCase(name)) {
                    validate = false;
                }
            }

            if (validate && ctx.inventory.contains(item.getId())) {
                item.interact("Deposit-All");
                Utilities.sleep(200, 450);
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositInventory() {
        Widget widget = ctx.widgets.find(BANK_ID, DEPOSIT_INVENTORY_ID).single();
        if (widget != null) {
            widget.click(true);
            Utilities.sleepUntil(new StatePredicate() {
                @Override
                public boolean apply() {
                    return ctx.inventory.isEmpty();
                }
            }, 3000);
        }
        return ctx.inventory.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean depositEquipment() {
        Widget widget = ctx.widgets.find(BANK_ID, DEPOSIT_EQUIPMENT_ID).single();
        if (widget != null) {
            widget.click(true);
            Utilities.sleepUntil(new StatePredicate() {
                @Override
                public boolean apply() {
                    return ctx.equipment.isEmpty();
                }
            }, 3000);
        }
        return ctx.equipment.isEmpty();
    }

    public boolean canOpen(Tab tab) {
        return !isOpen(tab) && getTabCount() - 1 >= tab.getWidgetId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTabCount() {
        if (!isOpen()) {
            return -1;
        }
        Widget tabs = getTabWidget();
        if (tabs != null) {
            for (Widget child : tabs.getChildren()) {
                if (child.getWidth() == 36 &&
                        child.getHeight() == 32 && child.getId() != 10) {
                    return child.getId() - 11;
                }
            }
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tab getCurrent() {
        if (!isOpen()) {
            return null;
        }
        Widget widget = ctx.widgets.find(BANK_ID, TITLE_ID).single();
        if (widget != null) {
            String text = widget.getText();
            if (!text.contains("Tab")) {
                return Tab.ALL;
            }
            return Tab.forId(Integer.parseInt(text.split(" ")[1]));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createTab(WidgetItem item) {
        if (!isOpen() || !contains(item.getId()) || getTabCount() == 9) {
            return false;
        }
        final int count = getTabCount();
        ctx.mouse.press(item.getClickPoint());
        ctx.mouse.drag(getTabWidget().getChild(count + 1).getClickPoint());
        ctx.mouse.release(getTabWidget().getChild(count + 1).getClickPoint());
        Utilities.sleepUntil(new StatePredicate() {
            @Override
            public boolean apply() {
                return getTabCount() == count + 1;
            }
        }, 3000);
        return getTabCount() == count + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen(Tab tab) {
        return getCurrent() == tab;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createTab(int id) {
        return createTab(find(id).single());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createTab(String name) {
        return createTab(find(name).single());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean openTab(final Tab tab) {
        if (!isOpen() || tab.getWidgetId() > getTabCount()) {
            return false;
        }
        getTabWidget().getChild(tab.getWidgetId()).click(true);
        Utilities.sleepUntil(new StatePredicate() {
            @Override
            public boolean apply() {
                return getCurrent() == tab;
            }
        }, 1500);
        return getCurrent() == tab;
    }

    private int getOpenTextureID(Widget[] widgets) {
        int[] textureIDs = new int[widgets.length];
        Map<Integer, Integer> frequency = new HashMap<Integer, Integer>();
        for (int i = 0; i < widgets.length; i++) {
            textureIDs[i] = widgets[i].getTextureId();
            if (frequency.containsKey(textureIDs[i])) {
                int freq = frequency.get(textureIDs[i]);
                frequency.remove(textureIDs[i]);
                frequency.put(textureIDs[i], freq + 1);
            } else {
                frequency.put(textureIDs[i], 1);
            }
        }
        return extractSingleton(frequency);
    }

    private <T> T extractSingleton(Map<T, Integer> items) {
        for (Map.Entry<T, Integer> entries : items.entrySet()) {
            if (entries.getValue() == 1) {
                return entries.getKey();
            }
        }
        return null;
    }

    private final StatePredicate walking = new StatePredicate() {
        @Override
        public boolean apply() {
            return !ctx.players.getLocal().isMoving();
        }
    };

    private final StatePredicate closed = new StatePredicate() {
        @Override
        public boolean apply() {
            return !isOpen();
        }
    };

    public static enum TransferPolicy {

        PER_ONE, PER_FIVE, PER_10, PER_X

    }

    public enum Tab {
        ALL(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5),
        SIX(6), SEVEN(7), EIGHT(8), NINE(9);

        private int widgetId;

        Tab(int widgetId) {
            this.widgetId = widgetId;
        }

        public int getWidgetId() {
            return widgetId;
        }

        public static Tab forId(int id) {
            switch (id) {
                case 0:
                    return ALL;
                case 1:
                    return ONE;
                case 2:
                    return TWO;
                case 3:
                    return FOUR;
                case 4:
                    return FOUR;
                case 5:
                    return FIVE;
                case 6:
                    return SIX;
                case 7:
                    return SEVEN;
                case 8:
                    return EIGHT;
                case 9:
                    return NINE;
            }
            return null;
        }
    }
}
