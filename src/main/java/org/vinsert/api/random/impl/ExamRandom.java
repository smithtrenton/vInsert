package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.Npc;
import org.vinsert.api.wrappers.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : const_
 */
@LoginRequired
@RandomManifest(name = "Exam Solver", author = "const_", version = 0.1)
public final class ExamRandom extends RandomSolver {

    private static final String NPC_NAME = "Mr. Mordaut";
    private static final int PATTERN_WIDGET_GROUP = 103;
    private static final int PATTERN_WIDGET_CHILD_1 = 8;
    private static final int PATTERN_WIDGET_CHILD_3 = 10;
    private static final int BOARD_WIDGET_TEXT = 72;
    private static final int BOARD_WIDGET_GROUP = 559;
    private int currentDoor = 0;
    private static final int[] ranged = {11539, 11540, 11541, 11614, 11615, 11633};

    private static final int[] cooking = {11526, 11529, 11545, 11549, 11550, 11555, 11560,
            11563, 11564, 11607, 11608, 11616, 11620, 11621, 11622, 11623,
            11628, 11629, 11634, 11639, 11641, 11649, 11624};

    private static final int[] fishing = {11527, 11574, 11578, 11580, 11599, 11600, 11601,
            11602, 11603, 11604, 11605, 11606, 11625};

    private static final int[] combat = {11528, 11531, 11536, 11537, 11579, 11591, 11592,
            11593, 11597, 11627, 11631, 11635, 11636, 11638, 11642, 11648};

    private static final int[] farming = {11530, 11532, 11547, 11548, 11554, 11556, 11571,
            11581, 11586, 11610, 11645};

    private static final int[] _magic = {11533, 11534, 11538, 11562, 11567, 11582};

    private static final int[] firemaking = {11535, 11551, 11552, 11559, 11646};

    private static final int[] hats = {11540, 11557, 11558, 11560, 11570, 11619, 11626,
            11630, 11632, 11637, 11654};
    private static final int[] pirate = {11570, 11626, 11558};

    private static final int[] jewellery = {11572, 11576, 11652};

    private static final int[] drinks = {11542, 11543, 11544, 11644, 11647};

    private static final int[] woodcutting = {11573, 11595};

    private static final int[] boots = {11561, 11618, 11650, 11651};

    private static final int[] crafting = {11546, 11553, 11565, 11566, 11568, 11569, 11572,
            11575, 11576, 11577, 11581, 11583, 11584, 11585, 11643, 11652,
            11653};

    private static final int[] mining = {11587, 11588, 11594, 11596, 11598, 11609, 11610};

    private static final int[] smithing = {11611, 11612, 11613};
    private static final int[][] items = {ranged, cooking, fishing, combat, farming, _magic,
            firemaking, hats, drinks, woodcutting, boots, crafting, mining,
            smithing};
    public Question[] simQuestions = {
            new Question("I never leave the house without some sort of jewellery.", jewellery),
            new Question("Pretty accessories made from silver and gold", jewellery),
            new Question("There is no better feeling than", jewellery),
            new Question("I'm feeling dehydrated", drinks),
            new Question("All this work is making me thirsty", drinks),
            new Question("quenched my thirst", drinks),
            new Question("light my fire", firemaking),
            new Question("fishy", fishing),
            new Question("fish", fishing),
            new Question("seafood", fishing),
            new Question("fishing for answers", fishing),
            new Question("fish out of water", drinks),
            new Question("strange headgear", hats),
            new Question("tip my hat", hats),
            new Question("thinking cap", hats),
            new Question("wizardry here", _magic),
            new Question("rather mystical", _magic),
            new Question("abracada", _magic),
            new Question("hide one's face", hats),
            new Question("shall unmask", hats),
            new Question("hand-to-hand", combat),
            new Question("hate melee and magic", combat),
            new Question("hate ranging and magic", combat),
            new Question("melee weapon", combat),
            new Question("prefers melee", combat),
            new Question("me hearties", pirate),
            new Question("puzzle for landlubbers", pirate),
            new Question("mighty pirate", pirate),
            new Question("mighty archer", ranged),
            new Question("as an arrow", ranged),
            new Question("ranged attack", ranged),
            new Question("shiny things", crafting),
            new Question("igniting", firemaking),
            new Question("sparks from my synapses.", firemaking),
            new Question("fire.", firemaking),
            new Question("disguised", hats),
            new Question("range", ranged),
            new Question("arrow", ranged),
            new Question("drink", drinks),
            new Question("logs", firemaking),
            new Question("light", firemaking),
            new Question("headgear", hats),
            new Question("hat", hats),
            new Question("cap", hats),
            new Question("mine", mining),
            new Question("mining", mining),
            new Question("ore", mining),
            new Question("fish", fishing),
            new Question("fishing", fishing),
            new Question("thinking cap", hats),
            new Question("cooking", cooking),
            new Question("cook", cooking),
            new Question("bake", cooking),
            new Question("farm", farming),
            new Question("farming", farming),
            new Question("cast", _magic),
            new Question("magic", _magic),
            new Question("craft", crafting),
            new Question("boot", boots),
            new Question("chop", woodcutting),
            new Question("cut", woodcutting),
            new Question("tree", woodcutting),
    };

    @Override
    public boolean canRun() {
        return npcs.find(NPC_NAME).exists();
    }

    @Override
    public int run() {
        final Npc dragon = npcs.find(NPC_NAME).single();
        if (currentDoor > 0) {
            final GameObject door = objects.find(currentDoor).single();
            if (door != null) {
                if (door.isOnScreen()) {
                    camera.setPitch(true);
                    door.interact("Open");
                    currentDoor = 0;
                }
            }
        } else if (widgets.canContinue()) {
            Widget door = widgets.find().text("door").single();
            if (door != null && door.getText() != null) {
                String text = door.getText();
                if (text.toLowerCase().contains("purple")) {
                    currentDoor = getDoorIds().get(2);
                } else if (text.toLowerCase().contains("red")) {
                    currentDoor = getDoorIds().get(0);
                } else if (text.toLowerCase().contains("green")) {
                    currentDoor = getDoorIds().get(3);
                } else if (text.toLowerCase().contains("blue")) {
                    currentDoor = getDoorIds().get(1);
                }
            }
            widgets.clickContinue();
        } else if (widgets.find(BOARD_WIDGET_GROUP, 0).exists()) {
            List<Widget> children = getWidgetWith(getSolve());
            for (Widget child : children) {
                child.click(true);
                Utilities.sleep(1000, 2000);
            }
            if (widgets.find(BOARD_WIDGET_GROUP, 0).exists()) {
                widgets.find(BOARD_WIDGET_GROUP, 70).single().click(true);
                Utilities.sleepUntil(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return widgets.canContinue();
                    }
                }, 3000);
            }
        } else if (widgets.find(PATTERN_WIDGET_GROUP, PATTERN_WIDGET_CHILD_1).exists()) {
            if (getSolve()[0] > 0) {
                Widget solve = getWidgetWith(getSolve()[0]);
                solve.click(true);
            } else {
                widgets.find(PATTERN_WIDGET_GROUP, Utilities.random(10, 13)).single().click(true);
            }
            Utilities.sleepUntil(new StatePredicate() {
                @Override
                public boolean apply() {
                    return !widgets.find(PATTERN_WIDGET_GROUP, PATTERN_WIDGET_CHILD_1).exists() ||
                            !widgets.find(PATTERN_WIDGET_GROUP, PATTERN_WIDGET_CHILD_1).single().isValid();
                }
            }, 3000);
        } else {
            if (dragon != null) {
                switch (dragon.interact("Talk-to")) {
                    case OK:
                        Utilities.sleepUntil(new StatePredicate() {
                            @Override
                            public boolean apply() {
                                return widgets.canContinue();
                            }
                        }, 3000);
                        return 500;
                    default:
                        camera.setPitch(true);
                        walking.walk(dragon);
                }
            }
        }
        return 600;
    }

    private List<Integer> getDoorIds() {
        final List<Integer> doorIdList = new ArrayList<>();
        final List<GameObject> doorObjects = objects.find().nameContains("door").asList();
        for (final GameObject o : doorObjects) {
            if (o != null) {
                doorIdList.add(o.getId());
            }
        }
        Collections.sort(doorIdList);
        return doorIdList;
    }


    private List<Widget> getWidgetWith(int[] id) {
        ArrayList<Widget> widgetChildren = new ArrayList<>();
        for (int i = 0; i < widgets.getGroup(BOARD_WIDGET_GROUP).getAll().length; i++) {
            if (contains(id, widgets.find(BOARD_WIDGET_GROUP, i).single().getModelId())) {
                widgetChildren.add(widgets.find(BOARD_WIDGET_GROUP, i).single());
            }
        }
        return widgetChildren;
    }

    private Widget getWidgetWith(int id) {
        for (int i = PATTERN_WIDGET_CHILD_3 + 1; i < widgets.getGroup(PATTERN_WIDGET_GROUP).getAll().length; i++) {
            if (widgets.find(PATTERN_WIDGET_GROUP, i).single().getModelId() == id) {
                return widgets.find(PATTERN_WIDGET_GROUP, i).single();
            }
        }
        return null;
    }

    private int[] getSolve() {
        if (widgets.find(PATTERN_WIDGET_GROUP, PATTERN_WIDGET_CHILD_3).exists()) {
            for (int[] category : items) {
                int count = 0;
                for (int i = 0; i < widgets.getGroup(PATTERN_WIDGET_GROUP).getAll().length; i++) {
                    if (contains(category, widgets.find(PATTERN_WIDGET_GROUP, i).single().getModelId())) {
                        if (count == 3) {
                            return new int[]{widgets.find(PATTERN_WIDGET_GROUP, i).single().getModelId()};
                        }
                        count++;
                    }
                }
            }
        } else if (widgets.find(BOARD_WIDGET_GROUP, BOARD_WIDGET_TEXT).exists() &&
                widgets.find(BOARD_WIDGET_GROUP, BOARD_WIDGET_TEXT).single().getText() != null) {
            String question = widgets.find(BOARD_WIDGET_GROUP, BOARD_WIDGET_TEXT).single().getText();
            for (Question q : simQuestions) {
                if (question.toLowerCase().contains(q.getQuestion().toLowerCase())) {
                    return q.getAnswers();
                }
            }
        }
        return new int[1];
    }

    private boolean contains(int[] array, int i) {
        for (int d : array) {
            if (i == d) {
                return true;
            }
        }
        return false;
    }

    private static final class Question {

        String question;
        int[] items;

        private Question(String question, int[] items) {
            this.question = question;
            this.items = items;
        }

        public String getQuestion() {
            return this.question;
        }

        public int[] getAnswers() {
            return this.items;
        }
    }

}