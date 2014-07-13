package org.vinsert.api.impl.scene;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Npcs;
import org.vinsert.api.collection.queries.NpcQuery;
import org.vinsert.api.wrappers.Npc;
import org.vinsert.game.engine.renderable.entity.INpc;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * API implementation for accessing RuneScape NPCs
 */
public final class NpcsImpl implements Npcs {
    private final MethodContext ctx;

    @Inject
    public NpcsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Npc> getAll() {
        List<Npc> npcs = newArrayList();
        INpc[] npcArray = ctx.client.getNpcs();
        for (INpc npc : npcArray) {
            if (npc == null) {
                continue;
            }
            npcs.add(new Npc(ctx, npc));
        }
        return npcs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NpcQuery find() {
        return new NpcQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NpcQuery find(int... ids) {
        return find().id(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NpcQuery find(String... names) {
        return find().named(names);
    }
}
