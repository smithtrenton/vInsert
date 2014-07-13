package org.vinsert.api.impl.tabs;

import com.google.inject.Inject;
import org.vinsert.api.Equipment;
import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.Filter;
import org.vinsert.api.collection.queries.EquipmentQuery;
import org.vinsert.api.collection.queries.InventoryQuery;
import org.vinsert.api.wrappers.Tab;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetGroup;
import org.vinsert.api.wrappers.WidgetItem;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Equipment interaction implementation
 *
 * @author tommo
 */
public final class EquipmentImpl implements Equipment {
    private static final int GROUP_ID = 387;
    private static final int CHILD_ID = 1;
    private static final String EQUIP_REGEX = "(Wear|Wield|Equip)";
    private static final String UNEQUIP_REGEX = "Remove";

    private final MethodContext ctx;

    @Inject
    public EquipmentImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<WidgetItem> getAll() {
        List<WidgetItem> list = newArrayList();
        WidgetGroup group = ctx.widgets.getGroup(GROUP_ID);
        ctx.tabs.open(Tab.EQUIPMENT);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Widget item = group.getAll()[slot.widgetId];
            Widget holder = item.getChild(CHILD_ID);
            if (holder != null && holder.getItemId() != -1) {
                WidgetItem slotItem = new WidgetItem(ctx, item.getArea(),
                        holder.getItemId(), holder.getItemStackSize(), item, slot.widgetId, WidgetItem.Type.EQUIPMENT);
                list.add(slotItem);
            }
        }
        return list;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getWidget() {
        return ctx.widgets.find(GROUP_ID, 0).single();
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
    public int getCount() {
        return getAll().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFull() {
        return getCount() == EquipmentSlot.values().length;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public void equipAll(int... ids) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        for (WidgetItem item : ctx.inventory.getAll()) {
            if (ctx.inventory.contains(item.getId())) {
                item.interact(EQUIP_REGEX);
            }
        }
    }

    @Override
    public void equipAll(Filter<WidgetItem> predicate) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        for (WidgetItem item : ctx.inventory.getAll()) {
            if (ctx.inventory.contains(item.getId()) && (predicate == null || predicate.accept(item))) {
                item.interact(EQUIP_REGEX);
            }
        }
    }

    @Override
    public void equipAll(String... names) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        InventoryQuery query = ctx.inventory.find(names);
        if (query.exists()) {
            for (WidgetItem item : query.asList()) {
                item.interact(EQUIP_REGEX);
            }
        }
    }

    @Override
    public void equip(int id) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        InventoryQuery query = ctx.inventory.find(id);
        if (query.exists()) {
            query.single().interact(EQUIP_REGEX);
        }
    }

    @Override
    public void equip(String name) {
        if (!ctx.tabs.isOpen(Tab.INVENTORY)) {
            ctx.tabs.open(Tab.INVENTORY);
        }

        InventoryQuery query = ctx.inventory.find(name);
        if (query.exists()) {
            query.single().interact(EQUIP_REGEX);
        }
    }

    @Override
    public void unequipAll(int... ids) {
        if (!ctx.tabs.isOpen(Tab.EQUIPMENT)) {
            ctx.tabs.open(Tab.EQUIPMENT);
        }

        Collection<WidgetItem> items = getAll();
        for (WidgetItem item : items) {
            if (contains(item.getId())) {
                item.interact(UNEQUIP_REGEX);
            }
        }
    }

    @Override
    public void unequipAll(Filter<WidgetItem> predicate) {
        if (!ctx.tabs.isOpen(Tab.EQUIPMENT)) {
            ctx.tabs.open(Tab.EQUIPMENT);
        }

        for (WidgetItem item : getAll()) {
            if (contains(item.getId()) && (predicate == null || predicate.accept(item))) {
                item.interact(UNEQUIP_REGEX);
            }
        }
    }

    @Override
    public void unequipAll(String... names) {
        if (!ctx.tabs.isOpen(Tab.EQUIPMENT)) {
            ctx.tabs.open(Tab.EQUIPMENT);
        }

        EquipmentQuery query = find(names);
        if (query.exists()) {
            for (WidgetItem item : query.asList()) {
                item.interact(UNEQUIP_REGEX);
            }
        }
    }

    @Override
    public void unequip(int id) {
        if (!ctx.tabs.isOpen(Tab.EQUIPMENT)) {
            ctx.tabs.open(Tab.EQUIPMENT);
        }

        EquipmentQuery query = find(id);
        if (query.exists()) {
            query.single().interact(UNEQUIP_REGEX);
        }
    }

    @Override
    public void unequip(String name) {
        if (!ctx.tabs.isOpen(Tab.EQUIPMENT)) {
            ctx.tabs.open(Tab.EQUIPMENT);
        }

        EquipmentQuery query = find(name);
        if (query.exists()) {
            query.single().interact(EQUIP_REGEX);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquipmentQuery find() {
        return new EquipmentQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquipmentQuery find(int... ids) {
        return find().id(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquipmentQuery find(String... names) {
        return find().named(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquipmentQuery find(EquipmentSlot... slots) {
        return find().slot(slots);
    }

    /**
     * Represents a slot in the player's equipment interface
     *
     * @author tommo
     */
    public static enum EquipmentSlot {
        /**
         * The head slot.
         */
        HEAD(6),
        /**
         * The cape slot.
         */
        CAPE(7),
        /**
         * The neck slot.
         */
        NECK(8),
        /**
         * The quiver slot.
         */
        QUIVER(16),
        /**
         * The weapon slot.
         */
        WEAPON(9),
        /**
         * The chest slot.
         */
        CHEST(10),
        /**
         * The shield slot.
         */
        SHIELD(11),
        /**
         * The legs slot.
         */
        LEGS(12),
        /**
         * The hands slot.
         */
        HANDS(13),
        /**
         * The feet slot.
         */
        FEET(14),
        /**
         * The ring slot.
         */
        RING(15);

        /**
         * The widget id for this slot.
         */
        private int widgetId;

        /**
         * Creates an equipment slot
         *
         * @param widgetId the widget id
         */
        EquipmentSlot(int widgetId) {
            this.widgetId = widgetId;
        }

        /**
         * Returns the child widget id in the equipment interface.
         *
         * @return The index in the equipment {@link org.vinsert.api.wrappers.WidgetGroup} of the {@link Widget} which visually represents this slot
         */
        public int widgetId() {
            return widgetId;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        public String toString() {
            char c = name().charAt(0);
            return c + name().toLowerCase().substring(1);
        }
    }
}
