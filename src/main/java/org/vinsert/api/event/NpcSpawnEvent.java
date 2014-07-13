package org.vinsert.api.event;


import org.vinsert.game.engine.renderable.entity.INpc;

/**
 *
 */
public final class NpcSpawnEvent {
    private INpc npc;

    public NpcSpawnEvent(INpc npc) {
        this.npc = npc;
    }

    public INpc getNpc() {
        return npc;
    }

}
