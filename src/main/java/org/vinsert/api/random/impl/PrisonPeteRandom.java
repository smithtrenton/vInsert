package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.collection.queries.LootQuery;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * ShadowBot Port of Prison Pete
 */
@LoginRequired
@RandomManifest(name = "Prison Pete", version = 1.0)
public final class PrisonPeteRandom extends RandomSolver {
    private static final int LEVER_ID = 24296;
    private static final int WIDGET_ID = 273;
    private static final int KEY_ID = 6966;
    private boolean leave = false;
    private int droppedItem = -1;

    private Balloon currentBalloon;
    private String state = "";

    private enum Balloon {

        ONE_STICK_TAIL(210, 10750), ONE_STICK(230, 10749), THREE_STICK(320, 11034), FOUR_STICK(282,
                10751);
        int id;
        int widgetId;

        Balloon(final int id, final int widgetId) {
            this.id = id;
            this.widgetId = widgetId;
        }
    }

    @Override
    public boolean canRun() {
        boolean peteExists = npcs.find("Prison Pete").exists();
        if (!peteExists) {
            currentBalloon = null;
            droppedItem = -1;
        }
        return peteExists;
    }

    private boolean isSpeaking() {
        boolean speaking = false;
        final Widget parent = widgets.find(242, 1).visible().single();
        if (parent != null) {
            final String text = parent.getText();
            if (text != null) {
                if (text.toLowerCase().contains("prison pete")) {
                    speaking = true;
                }
            }
        }
        return speaking;
    }

    @Override
    public int run() {
        if (!leave) {
            state = "Cycle start";
            if (inventory.contains(KEY_ID)) {
                state = "Inventory contains a key.. finding prison pete.";
                final Npc pete = npcs.find("Prison Pete").single();
                if (pete != null) {
                    currentBalloon = null;
                    switch (pete.interact("Talk")) {
                        case OK:
                            state = "Talked to prison pete, sleep until continue.";
                            Utilities.sleepUntil(new StatePredicate() {
                                @Override
                                public boolean apply() {
                                    return widgets.canContinue();
                                }
                            }, 1000);
                            int times = 1;
                            while (widgets.canContinue()) {
                                state = "Clicking continue (" + times + ")";
                                widgets.clickContinue();
                                Utilities.sleep(600);
                                times++;
                            }
                            return 1000;

                        case NOT_ON_SCREEN:
                            state = "Walking to pete";
                            walking.walk(pete);
                            return 1000;

                        case NOT_IN_MENU:
                        case MISSED:
                            state = "Missed pete";
                            return 50;
                    }
                }
            }

            if (currentBalloon != null && widgets.getGroup(WIDGET_ID) != null) {
                state = "Closing interface";
                switch (widgets.find(WIDGET_ID, 6).single().interact("")) {
                    case OK:
                        return 600;

                    default:
                        return 50;
                }
            } else if (widgets.canContinue()) {
                state = "We can continue.";
                final Widget parent = widgets.find(242, 2).single();
                if (parent != null && parent.isValid()) {
                    final String text = parent.getText();
                    if (text != null) {
                        String didIt = "You did it, you got all the keys right!";
                        String leaveString = "Come on, we should leave before Evil Bob comes back!";
                        if (text.equalsIgnoreCase(didIt) || text.equalsIgnoreCase(leaveString)) {
                            leave = true;
                        }
                    }
                }
                widgets.clickContinue();
            } else if (!inventory.contains(KEY_ID)) {
                state = "No key in inventory :(.";
                if (currentBalloon == null) {
                    GameObject lever = objects.find(LEVER_ID).single();
                    if (widgets.getGroup(WIDGET_ID) != null) {
                        currentBalloon = deterBalloon(widgets.find(WIDGET_ID, 3).single().getModelId());
                    } else if (lever != null) {
                        camera.setAngle(Utilities.random(0, 180));
                        if (lever.isOnScreen()) {
                            state = "Interacting with lever";
                            lever.interact("Pull");
                            Utilities.sleepUntil(new StatePredicate() {
                                @Override
                                public boolean apply() {
                                    return widgets.getGroup(WIDGET_ID) != null;
                                }
                            }, 2000);
                        } else {
                            walking.walk(lever);
                        }
                    }

                } else {
                    state = "Interacting with balloon";
                    Npc balloon = npcs.find().visible().hasModel().distance(7)
                            .vertexCount(currentBalloon.id).single();
                    if (balloon != null) {
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
                        switch (balloon.interact("Pop")) {
                            case OK:
                                return 1000;

                            case NOT_ON_SCREEN:
                                walking.walk(balloon);
                                return 600;

                            case NOT_IN_MENU:
                            case MISSED:
                                return 50;
                        }
                    }
                }
            } else {
                state = "Unhandled state..";
            }
        } else {
            state = "Trying to leave.";
            leave();
        }

        return Utilities.random(150, 200);
    }

    private Balloon deterBalloon(int id) {
        for (Balloon balloon : Balloon.values()) {
            if (balloon.widgetId == id) {
                return balloon;
            }
        }
        return null;
    }

    private void leave() {
        final LootQuery itemQuery = loot.find(droppedItem);
        Loot item = itemQuery.single();
        if (item != null) {
            if (inventory.isFull()) {
                if (inventory.contains(KEY_ID)) {
                    inventory.dropAll(KEY_ID);
                }
            }
            if (!inventory.isFull()) {
                switch (item.interact("Take")) {
                    case OK:
                        Utilities.sleepUntil(new StatePredicate() {
                            @Override
                            public boolean apply() {
                                return !itemQuery.exists();
                            }
                        }, 3000);
                    default:
                        camera.setPitch(true);
                        walking.walk(item);
                        item.interact("Take");
                }
            }
        }
        GameObject lever = objects.find(LEVER_ID).single();
        if (!widgets.canContinue()) {
            if (lever != null) {
                final Tile exit = new Tile(lever.getTile().getX() + 10, lever.getTile().getY() + 3);
                walking.walk(exit);
                for (int i = 0; i < 100 && player.isMoving(); i++) {
                    Utilities.sleep(40, 60);
                }
            }
        } else {
            widgets.clickContinue();
        }
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics g = event.getGraphics();
        g.setColor(Color.YELLOW);
        g.drawString("Prison Pete: " + state, 10, 100);
    }


    private WidgetItem getLowestItem() {
        java.util.List<Integer> count = new ArrayList<>();
        for (WidgetItem item : inventory.getAll()) {
            if (item != null && item.getId() != 995) {
                count.add(inventory.getCount(true, item.getId()));
            }
        }
        return inventory.getAll().get(count.indexOf(Collections.min(count)));
    }
}
