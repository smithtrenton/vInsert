package org.vinsert.api.random.impl;

import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;

/**
 * @author : const_
 */
@LoginRequired
@RandomManifest(name = "Run Away Solver", version = 1.0)
public final class RunAwayRandom extends RandomSolver {

    private static final String[] NAMES = {"Tree spirit", "Strange plant", "Rock Golem",
            "Evil Chicken", "Mr Hyde"};

    @Override
    public boolean canRun() {
        return npcs.find(NAMES).interacting(players.getLocal()).exists();
    }

    @Override
    public int run() {
        //TODO
        return 500;
    }
}
