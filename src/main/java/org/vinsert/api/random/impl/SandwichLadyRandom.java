package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Npc;
import org.vinsert.api.wrappers.Widget;

/**
 * @author : const_
 */
@LoginRequired
@RandomManifest(name = "Sandwich Lady Solver", author = "const_", version = 0.1)
public final class SandwichLadyRandom extends RandomSolver {

    private static final int WIDGET_GROUP = 297;
    private static final int WIDGET_CHILD = 8;

    @Override
    public boolean canRun() {
        return npcs.find("Sandwich Lady").messageContains(player.getName()).exists();
    }

    @Override
    public int run() {
        Npc npc = npcs.find("Sandwich Lady").single();
        if (widgets.getGroup(WIDGET_GROUP) == null && !player.isInteracting() && npc != null) {
            npc.interact("Talk-to");
            Utilities.sleepUntil(new StatePredicate() {
                @Override
                public boolean apply() {
                    return widgets.getGroup(WIDGET_GROUP) != null;
                }
            }, 3000);
            return 500;
        } else if (widgets.getGroup(WIDGET_GROUP) == null && widgets.canContinue() ||
                !widgets.find(WIDGET_GROUP, WIDGET_CHILD).exists()) {
            widgets.clickContinue();
            Utilities.sleepUntil(new StatePredicate() {
                @Override
                public boolean apply() {
                    return widgets.find(WIDGET_GROUP, WIDGET_CHILD).exists();
                }
            }, 3000);
            return 50;
        } else {
            String whatSandwich = widgets.find(WIDGET_GROUP, WIDGET_CHILD).single().getText();
            Widget sandwich = getSandwichComponent(Sandwiches.getObject(whatSandwich).getId());
            if (sandwich != null) {
                sandwich.click(true);
                Utilities.sleepUntil(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return !widgets.find(WIDGET_GROUP, WIDGET_CHILD).exists();
                    }
                }, 3000);
            }
            return 500;
        }
    }

    private enum Sandwiches {

        SQUARE(10731, "square"),
        ROLL(10727, "roll"),
        CHOCOLATE(10728, "chocolate"),
        BAGUETTE(10726, "baguette"),
        TRIANGLE(10732, "triangle"),
        KEBAB(10729, "kebab"),
        PIE(10730, "pie");
        private final int modelId;
        private final String name;

        Sandwiches(int m, String n) {
            this.modelId = m;
            this.name = n;
        }

        public int getId() {
            return modelId;
        }

        public String getMessage() {
            return name;
        }

        public static Sandwiches getObject(String nm) {
            for (Sandwiches e : Sandwiches.values()) {
                if (nm.equals(e.getMessage())) {
                    return e;
                }
            }
            return null;
        }
    }

    public Widget getSandwichComponent(int mid) {
        Widget sandwich;
        for (int i = 0; i < widgets.getGroup(WIDGET_GROUP).getAll().length; i++) {
            sandwich = widgets.find(WIDGET_GROUP, i).single();
            if (sandwich != null) {
                if (sandwich.getModelId() == mid) {
                    return sandwich;
                }
            }
        }
        return null;
    }
}
