package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.collection.queries.WidgetQuery;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Tab;
import org.vinsert.api.wrappers.WidgetItem;
import org.vinsert.api.wrappers.interaction.Result;

/**
 * @author : const_
 */
@LoginRequired
@RandomManifest(name = "StrangeBox Solver", version = 1.0)
public final class StrangeBoxRandom extends RandomSolver {

    private static final int WIDGET_PARENT_ID = 190;
    private static final int WIDGET_CHILD_ID = 10;
    private static final int SETTINGS_ID = 312;

    @Override
    public boolean canRun() {
        return tabs.getCurrent() == Tab.INVENTORY &&
                inventory.contains("Strange box") && !bank.isOpen();
    }

    @Override
    public int run() {
        final WidgetQuery query = widgets.find(WIDGET_PARENT_ID, WIDGET_CHILD_ID);
        if (query.single() == null) {
            WidgetItem box = inventory.find("Strange box").single();
            if (box != null) {
                if (box.interact("Open") == Result.OK) {
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return query.single() != null;
                        }
                    }, 3000);
                }
            }
            return 500;
        }
        int answer = getWidgetIndex();
        final WidgetQuery answerQuery = widgets.find(WIDGET_PARENT_ID, answer);
        if (answerQuery.single() != null) {
            answerQuery.single().click(true);
            Utilities.sleepUntil(new StatePredicate() {
                @Override
                public boolean apply() {
                    return answerQuery.single() == null;
                }
            }, 4000);
        }
        return 50;
    }


    private int getWidgetIndex() {
        switch (settings.getWidgetSetting(SETTINGS_ID) >> 24) {
            case 0:
                return 10;
            case 1:
                return 11;
            case 2:
                return 12;
        }
        return 10;
    }
}
