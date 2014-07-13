package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Npc;
import org.vinsert.api.wrappers.interaction.Result;

/**
 * @author : const_
 */
@LoginRequired
@RandomManifest(name = "Talk-To Solver", version = 1.0)
public final class TalkToRandom extends RandomSolver {
    private static final String[] NAMES = {"Genie", "Niles", "Drunken Dwarf", "Highwayman", "Cap'n Hand", "Security Guard", "Rick Turpentine"};

    @Override
    public boolean canRun() {
        return npcs.find(NAMES).messageContains(player.getName()).exists() ||
                widgets.canContinue() && npcs.find(NAMES).exists() && npcs.find(NAMES).single().distanceTo(player) < 2
                || npcs.find(NAMES).interacting(players.getLocal()).exists();
    }

    @Override
    public int run() {
        if (widgets.canContinue()) {
            widgets.clickContinue();
            return 500;
        }
        final Npc npc = npcs.find(NAMES).interacting(players.getLocal()).single() != null ?
                npcs.find(NAMES).interacting(players.getLocal()).single() : npcs.find(NAMES).messageContains(player.getName()).single();
        if (npc != null && player.getInteractingEntity() != npc) {
            if (npc.interact("Talk-to") == Result.OK) {
                Utilities.sleepUntil(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return player.getInteractingEntity() == npc;
                    }
                }, 3000);
                return 500;
            }
        }
        return 500;
    }
}
