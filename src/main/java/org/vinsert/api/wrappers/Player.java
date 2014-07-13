package org.vinsert.api.wrappers;

import org.vinsert.api.MethodContext;
import org.vinsert.game.engine.renderable.entity.IPlayer;

/**
 * A wrapper for IPlayer
 */
public final class Player extends Entity implements Wrapper<IPlayer> {
    public Player(MethodContext ctx, IPlayer wrapped) {
        super(ctx, wrapped);
    }

    /**
     * Get the name of this player.
     *
     * @return name
     */
    @Override
    public String getName() {
        return unwrap().getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPlayer unwrap() {
        return (IPlayer) wrapped.get();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            return ((Player) obj).wrapped.equals(wrapped);
        }
        return super.equals(obj);
    }

    /**
     * Checks if a player is idle
     *
     * @return <t>true if they're not moving, interacting or in combat</t> otherwise false
     */
    public boolean isIdle() {
        return !isMoving() && !isInteracting() && !isInCombat() && getAnimationId() == -1;
    }
}
