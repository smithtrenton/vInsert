package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.collection.queries.LootQuery;
import org.vinsert.api.collection.queries.NpcQuery;
import org.vinsert.api.collection.queries.WidgetQuery;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.Loot;
import org.vinsert.api.wrappers.Npc;
import org.vinsert.api.wrappers.WidgetItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : const_
 */
@LoginRequired
@RandomManifest(name = "Freaky Forester Solver", author = "const_", version = 0.1)
public final class FreakyForesterRandom extends RandomSolver {

    private static final String NPC_NAME = "Freaky Forester";
    private static final String EXIT_PORTAL = "Exit portal";
    private static final String RAW_MEAT = "Raw pheasant";
    private int pheasantId = -1;
    private int droppedItem = -1;
    private boolean leave = false;

    private enum Pheasant {

        ONE_TAIL(310, "one"),
        TWO_TAIL(348, "two"),
        THREE_TAIL(378, "three"),
        FOUR_TAIL(408, "four");

        int length;
        String name;

        Pheasant(final int length, final String name) {
            this.length = length;
            this.name = name;
        }
    }

    @Override
    public boolean canRun() {
        if (!npcs.find(NPC_NAME).exists() || npcs.find("Tilt").distance(10).exists()) {
            pheasantId = -1;
            droppedItem = -1;
            leave = false;
            return false;
        }
        return true;
    }

    @Override
    public int run() {
        final LootQuery query = loot.find(RAW_MEAT);
        if (player.isMoving() || player.isInCombat()) {
            return 50;
        }
        if (widgets.canContinue()) {
            if (widgets.find().text("portal").visible().exists() ||
                    widgets.find().text("leave").visible().exists()) {
                leave = true;
            }
            final WidgetQuery wQuery = widgets.find().text("tails").visible();
            if (wQuery.exists()) {
                leave = false;
                pheasantId = getId(wQuery.single().getText());
            }
            widgets.clickContinue();
        } else if (leave) {
            final LootQuery itemQuery = loot.find(droppedItem);
            Loot item = itemQuery.single();
            if (item != null && droppedItem > 0) {
                if (inventory.isFull()) {
                    if (inventory.contains(RAW_MEAT)) {
                        inventory.dropAll(RAW_MEAT);
                        return 500;
                    }
                } else {
                    switch (item.interact("Take")) {
                        case OK:
                            Utilities.sleepUntil(new StatePredicate() {
                                @Override
                                public boolean apply() {
                                    return !itemQuery.exists();
                                }
                            }, 3000);
                            return 200;
                        default:
                            camera.setPitch(true);
                            walking.walk(item);
                            return 200;
                    }
                }
            } else {
                GameObject portal = objects.find(EXIT_PORTAL).single();
                if (portal != null) {
                    switch (portal.interact("Use")) {
                        case OK:
                            Utilities.sleepUntil(new StatePredicate() {
                                @Override
                                public boolean apply() {
                                    return !canRun();
                                }
                            }, 3000);
                            return 300;
                        default:
                            camera.setPitch(true);
                            walking.walk(portal);
                            return 200;
                    }
                }
            }
        } else if (inventory.contains(RAW_MEAT) || pheasantId < 1 &&
                !query.exists()) {
            Npc freakyForster = npcs.find(NPC_NAME).single();
            if (freakyForster != null) {
                switch (freakyForster.interact("Talk-to")) {
                    case OK:
                        Utilities.sleepUntil(new StatePredicate() {
                            @Override
                            public boolean apply() {
                                return widgets.canContinue();
                            }
                        }, 3000);
                        return 300;
                    default:
                        camera.setPitch(true);
                        walking.walk(freakyForster);
                        return 400;
                }
            }

        } else if (pheasantId > 0 || query.exists()) {
            Loot meat = query.single();
            if (meat != null) {
                if (inventory.isFull()) {
                    WidgetItem item = getLowestItem();
                    droppedItem = item.getId();
                    switch (item.interact("Drop")) {
                        case OK:
                            return 50;
                        default:
                            item.interact("Drop");
                            return 500;
                    }
                }
                switch (meat.interact("Take")) {
                    case OK:
                        Utilities.sleepUntil(new StatePredicate() {
                            @Override
                            public boolean apply() {
                                return !query.exists();
                            }
                        }, 3000);
                        return 150;
                    default:
                        camera.setPitch(true);
                        walking.walk(meat);
                        return 200;
                }
            } else {
                final NpcQuery pheasantQ = npcs.find(pheasantId);
                Npc pheasant = pheasantQ.single();
                if (pheasant != null) {
                    switch (pheasant.interact("Attack")) {
                        case OK:
                            Utilities.sleepUntil(new StatePredicate() {
                                @Override
                                public boolean apply() {
                                    return !pheasantQ.exists() && player.isIdle();
                                }
                            }, 10000);
                            pheasantId = 0;
                            return 200;
                        default:
                            camera.setPitch(true);
                            walking.walk(pheasant);
                    }
                }
            }
        }
        return 500;
    }

    private int getId(String message) {
        if (message.toLowerCase().contains("one") || message.toLowerCase().contains("1 tails")) {
            return getId(Pheasant.ONE_TAIL.length);
        } else if (message.toLowerCase().contains("two") || message.toLowerCase().contains("2 tails")) {
            return getId(Pheasant.TWO_TAIL.length);
        } else if (message.toLowerCase().contains("three") || message.toLowerCase().contains("3 tails")) {
            return getId(Pheasant.THREE_TAIL.length);
        } else if (message.toLowerCase().contains("four") || message.toLowerCase().contains("4 tails")) {
            return getId(Pheasant.FOUR_TAIL.length);
        }
        return -1;
    }

    private int getId(int model) {
        for (Npc npc : npcs.getAll()) {
            if (npc != null && npc.getModel() != null && npc.getModel().getPolygons().length == model) {
                return npc.getId();
            }
        }
        return -1;
    }

    private WidgetItem getLowestItem() {
        List<Integer> count = new ArrayList<>();
        for (WidgetItem item : inventory.getAll()) {
            if (item != null && item.getId() != 995) {
                count.add(inventory.getCount(true, item.getId()));
            }
        }
        return inventory.getAll().get(count.indexOf(Collections.min(count)));
    }
}

