package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.*;

@LoginRequired
@RandomManifest(name = "DrillDemon", version = 0.1)
public final class DrillDemonRandom extends RandomSolver {

    private Area randomArea = new Area(new Tile(3167, 4822), new Tile(3159, 4818));
    private static final int RANDOM_EVENT_SETTING = 531;
    private Exercise currentExercise;
    int[] settingIndexes = {2257, 794, 668, 1697, 1802, 1809, 675, 1305, 1676, 1249, 787, 2131, 738, 724, 1228, 2201, 1634, 2138, 1123, 1116, 1620, 2250, 2187, 1291};
    String[][] orders = {{"jog", "sit ups", "push ups", "star jumps"}, {"sit ups", "push ups", "star jumps", "jog"}, {"star jumps", "push ups", "sit ups", "jog"}, {"jog", "star jumps", "sit ups", "push ups"}, {"sit ups", "jog", "star jumps", "push ups"}, {"jog", "sit ups", "star jumps", "push ups"},
            {"push ups", "star jumps", "sit ups", "jog"}, {"jog", "push ups", "star jumps", "sit ups"}, {"star jumps", "jog", "sit ups", "push ups"}, {"jog", "star jumps", "push ups", "sit ups"}, {"push ups", "sit ups", "star jumps", "jog"}, {"push ups", "sit ups", "jog", "star jumps"},
            {"sit ups", "star jumps", "push ups", "jog"}, {"star jumps", "sit ups", "push ups", "jog"}, {"star jumps", "jog", "push ups", "sit ups"}, {"jog", "push ups", "sit ups", "star jumps"}, {"sit ups", "star jumps", "jog", "push ups"}, {"sit ups", "push ups", "jog", "star jumps"},
            {"push ups", "star jumps", "jog", "sit ups"}, {"star jumps", "push ups", "jog", "sit ups"}, {"star jumps", "sit ups", "jog", "push ups"}, {"sit ups", "jog", "push ups", "star jumps"}, {"push ups", "jog", "sit ups", "star jumps"}, {"push ups", "jog", "star jumps", "sit ups"}};

    @Override
    public boolean canRun() {
        if (randomArea.contains(player) || npcs.find("Sergeant Damien").exists()) {
            return true;
        }
        currentExercise = null;
        return false;
    }

    @EventHandler
    public void paint(PaintEvent event) {
        if (currentExercise == null) {
            return;
        }
        event.getGraphics().drawString("Current Exercise: " + currentExercise.getName(), 100, 100);
    }

    @Override
    public int run() {
        if (player.getAnimation() != -1) {
            currentExercise = null;
        }
        if (!player.isIdle()) {
            return 500;
        }
        if (currentExercise == null) {
            Widget textChild = widgets.find().text("Sergeant Damien").single();
            if (textChild != null) {
                for (Widget widget : widgets.getGroup(textChild.getGroup()).getAll()) {
                    if (currentExercise != null) {
                        break;
                    }
                    currentExercise = identifyExercise(widget.getText());
                }
                if (currentExercise == null && widgets.canContinue()) {
                    widgets.clickContinue();
                    return 500;
                }
                return 100;
            } else {
                Npc sergeant = npcs.find("Sergeant Damien").single();
                switch (sergeant.interact("Talk-to")) {
                    case OK:
                        Utilities.sleepWhile(new StatePredicate() {
                            @Override
                            public boolean apply() {
                                return player.isMoving();
                            }
                        }, 3600);
                        break;
                    default:
                        break;
                }
            }
            return 500;
        }
        ExerciseMat mat = getMatExercise();
        if (mat != null) {
            GameObject matObj = objects.find().location(mat.getLocation()).single();
            if (matObj != null) {
                switch (matObj.interact("Use")) {
                    case OK:
                        return 200;
                    default:
                        walking.walk(matObj);
                        return 200;
                }
            }
        }
        return 1000;
    }

    private Exercise identifyExercise(String msg) {
        msg = msg.toLowerCase();
        for (Exercise mat : Exercise.values()) {
            if (msg.contains(mat.getName())) {
                return mat;
            }
        }
        return null;
    }

    private ExerciseMat getMatExercise() {
     /*   offset = offset * 3;
        int matExercise = (settings.getWidgetSetting(RANDOM_EVENT_SETTING) >> offset & 7);*/
        int index = 0;
        for (int i = 0; i < settingIndexes.length; i++) {
            int setting = settingIndexes[i];
            if (setting == settings.getWidgetSetting(RANDOM_EVENT_SETTING)) {
                index = i;
                break;
            }
        }
        String[] order = orders[index];
        for (int i = 0; i < order.length; i++) {
            String excer = order[i];
            if (excer.equals(currentExercise.getName())) {
                return ExerciseMat.values()[i];
            }
        }
        return null;
    }

    private enum ExerciseMat {
        MAT_1(1, 3160, 4820),
        MAT_2(2, 3162, 4820),
        MAT_3(3, 3164, 4820),
        MAT_4(4, 3166, 4820);

        private final int index;
        private final int x;
        private final int y;

        private ExerciseMat(int index, int x, int y) {
            this.index = index;
            this.x = x;
            this.y = y;
        }

        public int getIndex() {
            return index;
        }

        public Tile getLocation() {
            return new Tile(x, y);
        }
    }

    private enum Exercise {
        STAR_JUMP(4, "star jumps"),
        PUSH_UPS(3, "push ups"),
        SIT_UPS(2, "sit ups"),
        jog_ON_SPOT(1, "jog");

        private final int mask;
        private final String name;

        private Exercise(int mask, String name) {
            this.mask = mask;
            this.name = name;
        }

        public int getBits() {
            return mask;
        }

        public String getName() {
            return name;
        }

        public static Exercise findExerciseByInt(int val) {
            for (int i = 0; i < Exercise.values().length; i++) {
                if (val == Exercise.values()[i].getBits()) {
                    return Exercise.values()[i];
                }
            }
            return null;
        }
    }

}