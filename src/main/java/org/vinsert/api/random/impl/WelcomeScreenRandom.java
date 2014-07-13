package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.wrappers.Widget;

import static org.vinsert.api.util.Utilities.sleepUntil;

/**
 *
 */
@RandomManifest(name = "Welcome Screen", version = 1.0)
public final class WelcomeScreenRandom extends RandomSolver {
    public static final int LOGIN_SCREEN_ROOT = 378;
    public static final int LOGIN_SCREEN_BUTTON = 6;

    @Override
    public boolean canRun() {
        return widgets.find(LOGIN_SCREEN_ROOT, LOGIN_SCREEN_BUTTON).exists();
    }

    @Override
    public int run() {
        Widget widget = widgets.find(LOGIN_SCREEN_ROOT, LOGIN_SCREEN_BUTTON).single();
        widget.click(true);
        sleepUntil(new StatePredicate() {
            @Override
            public boolean apply() {
                return !canRun();
            }
        }, 10000);
        if (camera.getPitch() < 270) {
            camera.setPitch(true);
        }
        return 1000;
    }
}
