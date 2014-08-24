package org.vinsert.api.wrappers;

import org.vinsert.api.MethodContext;
import org.vinsert.game.engine.renderable.entity.INpc;

/**
 * A wrapper for Npcs within the RuneScape client.
 */
public final class Npc extends Entity implements Wrapper<INpc> {
    private NpcComposite composite;

    public Npc(MethodContext ctx, INpc wrapped) {
        super(ctx, wrapped);
    }

    /**
     * Get the ID of this Npc.
     *
     * @return npc id
     */
    public int getId() {
        if (unwrap() != null && unwrap().getComposite() != null) {
            return unwrap().getComposite().getId();
        }
        return -1;
    }

    /**
     * Get the name of this Npc.
     *
     * @return name
     */
    @Override
    public String getName() {
        if (unwrap() != null && unwrap().getComposite() != null) {
            return unwrap().getComposite().getName();
        }
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public INpc unwrap() {
        return (INpc) wrapped.get();
    }

    /**
     * Gets the npc composite for this items id
     */
    public NpcComposite getComposite() {
        if (composite != null) {
            return composite;
        }

        composite = new NpcComposite(unwrap().getComposite());
        if (!composite.isValid()) {
            NpcComposite composite = context.composites.getNpcComposite(getId());
            if (composite.isValid() && composite.getModelIds() != null && composite.getModelIds().length > getRealIdIndex()) {
                composite = context.composites.getNpcComposite(getRealId());
                if (composite.isValid()) {
                    return (this.composite = composite);
                }
            }
        }
        return composite;
    }

    public int getRealId() {
        return unwrap().getComposite().getTransformIds()[unwrap().getComposite().getRealId()];
    }


    public int getRealIdIndex() {
        return unwrap().getComposite().getRealId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Npc) {
            return ((Npc) obj).wrapped.equals(wrapped);
        }
        getClass();
        return super.equals(obj);
    }
}
