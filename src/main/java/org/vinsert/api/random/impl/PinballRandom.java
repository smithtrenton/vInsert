package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.collection.queries.GameObjectQuery;
import org.vinsert.api.collection.queries.WidgetQuery;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.GameObject;

import java.util.List;

/**
 * @author : const_
 */
@LoginRequired
@RandomManifest(name = "Pinball Solver", version = 1.0)
public final class PinballRandom extends RandomSolver {
    private static final int WIDGET_SCORE = 263;
    private static final int WIDGET_CHILD = 1;
    private static final int SETTINGS_ID = 727;

    @Override
    public boolean canRun() {
        return objects.find("Pinball Post").exists()
                && objects.find("Cave Exit").exists() &&
                widgets.find(WIDGET_SCORE, WIDGET_CHILD).exists();
    }

    @Override
    public int run() {
        if (!player.isIdle()) {
            return 200;
        }
        if (widgets.canContinue()) {
            widgets.clickContinue();
            return 500;
        }
        final GameObjectQuery gameObjectQuery = objects.find("Cave Exit");
        GameObject exit = gameObjectQuery.single();
        final WidgetQuery widgetQuery = widgets.find(WIDGET_SCORE, WIDGET_CHILD);
        final int score = Integer.parseInt(widgetQuery.
                single().getText().split("Score: ")[1]);
        if (score < 10) {
            Pillar next = Pillar.get(settings.getWidgetSetting(SETTINGS_ID), score);
            if (next != null) {
                List<GameObject> pillars = objects.find("Pinball Post").asList();
                GameObject pillar = null;
                for (GameObject object : pillars) {
                    if (object.getX() - exit.getX() == next.getX()) {
                        pillar = object;
                        break;
                    }
                }
                if (pillar != null) {
                    switch (pillar.interact("Tag")) {
                        case NOT_ON_SCREEN:
                            walking.walk(pillar);
                            break;
                        case OK:
                            Utilities.sleepUntil(new StatePredicate() {
                                @Override
                                public boolean apply() {
                                    return !widgetQuery.single().getText()
                                            .contains(String.valueOf(score));
                                }
                            }, 6000);
                            break;
                        default:
                            camera.turnTo(pillar);
                            break;
                    }
                    return 500;
                }
            }
            return 250;
        }
        if (exit != null) {
            switch (exit.interact("Exit")) {
                case OK:
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return !gameObjectQuery.exists();
                        }
                    }, 3000);
                    break;
                default:
                    camera.setPitch(false);
                    camera.turnTo(exit);
                    break;
            }
        }
        return 500;
    }

    private enum Pillar {
        AIR(0, -5, 7490), FIRE(68, 0, 7495), EARTH(34, -3, 7492),
        WATER(136, 5, 8727), NATURE(102, 3, 8726);
        private int base;
        private int x;
        private int id;

        private Pillar(int base, int x, int id) {
            this.base = base;
            this.x = x;
            this.id = id;
        }

        private static Pillar get(int setting, int score) {
            for (Pillar pillar : values()) {
                if (pillar.getBase() + (512 * score) == setting) {
                    return pillar;
                }
            }
            return null;
        }

        public int getBase() {
            return base;
        }

        public int getId() {
            return id;
        }

        public int getX() {
            return x;
        }
    }
}
