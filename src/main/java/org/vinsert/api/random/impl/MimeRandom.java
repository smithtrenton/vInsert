package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Npc;
import org.vinsert.api.wrappers.Widget;

/**
 * @author const_
 */
@LoginRequired
@RandomManifest(name = "Mime Solver", version = 1.0)
public final class MimeRandom extends RandomSolver {

    private static final int WIDGET_PARENT_ID = 188;
    private Animation lastAnimation;
    private Animation lastPerformed;
    private static final String NAME = "Mime";


    @Override
    public int run() {
        Npc mime = npcs.find(NAME).single();
        if (mime != null) {
            Animation anim = Animation.forAnim(mime.getAnimationId());
            if (anim != null && anim.equals(lastAnimation)) {
                lastAnimation = anim;
            }
        }
        if (lastPerformed.equals(lastAnimation)) {
            return 50;
        }
        final Widget widget = widgets.find(WIDGET_PARENT_ID, lastAnimation.widget).single();
        if (widget != null) {
            if (widget.isValid()) {
                widget.click(true);
                Utilities.sleepUntil(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return player.getAnimation() != -1;
                    }
                }, 1200);
                Utilities.sleepUntil(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return player.getAnimation() == -1;
                    }
                }, 4000);
                lastPerformed = lastAnimation;
                return 50;
            }
        }
        return 100;
    }

    @Override
    public boolean canRun() {
        return npcs.find(NAME).exists();
    }

    private static enum Animation {

        CRY(860, 2),
        THINK(857, 3),
        LAUGH(861, 4),
        DANCE(866, 5),
        CLIMB_ROPE(1130, 6),
        LEAN(1129, 7),
        GLASS_WALL(1128, 8),
        GLASS_BOX(1131, 9);

        int anim, widget;

        Animation(int anim, int widget) {
            this.anim = anim;
            this.widget = widget;
        }

        static Animation forAnim(int anim) {
            for (Animation animation : values()) {
                if (anim == animation.anim) {
                    return animation;
                }
            }
            return null;
        }
    }
}