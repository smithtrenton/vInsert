package org.vinsert.api.random.impl;

import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;

/**
 *
 */
@RandomManifest(name = "Login", version = 1.0)
public final class LoginRandom extends RandomSolver {

    @Override
    public boolean canRun() {
        return client.getLoginIndex() == 10 && session.getEnvironment().hasAccount();
    }

    @Override
    public int run() {
        session.login(this);
        return 2500;
    }

}
