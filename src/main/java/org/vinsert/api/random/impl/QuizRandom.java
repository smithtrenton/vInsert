package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.collection.queries.WidgetQuery;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author : const_
 */
@LoginRequired
@RandomManifest(name = "Quiz Solver", author = "const_", version = 0.1)
public final class QuizRandom extends RandomSolver {

    private static final String NPC_NAME = "Quiz Master";

    @Override
    public boolean canRun() {
        return npcs.find(NPC_NAME).exists();
    }

    @Override
    public int run() {
        final WidgetQuery prize = widgets.find().text("Select your prize").visible();
        if (prize.exists()) {
            if (widgets.find().text("coins").visible().exists()) {
                widgets.find().text("coins").visible().single().click(true);
                Utilities.sleepUntil(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return !prize.exists();
                    }
                }, 3000);
                return 500;
            }
            for (Widget widget : widgets.getGroup(prize.single().getGroup()).getAll()) {
                if (widget != null && widget.getText() != null && !widget.getText().equals(prize.single().getText())) {
                    widget.click(true);
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return !prize.exists();
                        }
                    }, 3000);
                }
            }
            return 300;
        }
        WidgetQuery oddOneQ = widgets.find().text("Pick the odd one out.").visible();
        if (oddOneQ.exists()) {
            Widget widget = oddOneQ.single();
            if (widget != null) {
                WidgetGroup group = widgets.getGroup(widget.getGroup());
                if (group != null && group.getAll() != null &&
                        group.getAll().length > 0) {
                    Widget odd = findOdd(group);
                    if (odd != null) {
                        odd.click(true);
                        return 1000;
                    }
                }
            }
        }
        return 500;
    }

    private Widget findOdd(WidgetGroup group) {
        List<Integer> models = new ArrayList<>();
        for (int i = group.getAll().length - 4; i < group.getAll().length - 1; i++) {
            models.add(group.getAll()[i].getModelId());
        }
        Collections.sort(models, COMPARATOR);
        int max = 0;
        int modelId = 0;
        for (int id : models) {
            int total = 0;
            for (int id2 : models) {
                if (id2 == id) {
                    continue;
                }
                total += Math.abs((id - id2));
            }
            if (total > max) {
                max = total;
                modelId = id;
            }
        }
        for (Widget child : group.getAll()) {
            if (child.getModelId() == modelId) {
                return child;
            }
        }
        return null;
    }

    private static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    };
}
