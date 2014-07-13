package org.vinsert.api.wrappers;

import org.vinsert.api.Constants;
import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.interaction.SceneNode;
import org.vinsert.game.engine.renderable.entity.IEntity;
import org.vinsert.game.engine.renderable.entity.INpc;
import org.vinsert.game.engine.renderable.entity.IPlayer;

import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * An Entity in the RuneScape scene graph.
 * Examples of entities are:
 * - NPCs
 * - Players
 */
public abstract class Entity extends SceneNode {
    protected final WeakReference<IEntity> wrapped;
    private final MethodContext ctx;

    public Entity(MethodContext ctx, IEntity wrapped) {
        super(ctx);
        this.wrapped = new WeakReference<>(wrapped);
        this.ctx = ctx;
    }

    protected MethodContext getContext() {
        return ctx;
    }

    /**
     * Get the proc status of this entity in the logic loop
     *
     * @return loop cycle status
     */
    public int getLoopCycleStatus() {
        return unwrap().getLoopCycleStatus();
    }

    public IEntity unwrap() {
        return wrapped.get();
    }

    /**
     * Get the ID of the animation entity is currently performing.
     *
     * @return animation ID
     */
    public int getAnimationId() {
        return unwrap().getAnimationId();
    }

    /**
     * Get the current health for this entity.
     * Note: this is only available if the entity
     * is engaged in combat.
     *
     * @return current health
     */
    public int getCurrentHealth() {
        return unwrap().getCurrentHealth();
    }

    /**
     * Get the maximum possible health for this entity.
     *
     * @return max health
     */
    public int getMaxHealth() {
        return unwrap().getMaxHealth();
    }

    /**
     * Get the turn direction (model rotation) of this entity.
     *
     * @return turn direction
     */
    public int getOrientation() {
        return unwrap().getOrientation();
    }

    /**
     * Get the array index of the entity that is interacting with this entity.
     *
     * @return entity index
     */
    public int getAssociatedEntity() {
        return unwrap().getInteractingEntity();
    }

    /**
     * Get the Entity that is interacting with this one.
     *
     * @return Interacting Entity.
     */
    public Entity getInteractingEntity() {
        int index = getAssociatedEntity();
        if (index == -1) {
            return null;
        }

        if (index < 0x8000) {
            INpc accessor = ctx.client.getNpcs()[index];
            return accessor != null ? new Npc(ctx, accessor) : null;
        } else {
            IPlayer accessor = ctx.client.getPlayers()[index - 0x8000];
            if (accessor == null) {
                return ctx.players.getLocal();
            }
            return new Player(ctx, accessor);
        }
    }

    /**
     * Get the amount of steps remaining in the walking queue.
     *
     * @return path length
     */
    public int getQueueSize() {
        return unwrap().getQueueSize();
    }

    /**
     * Get the text of the last message spoken by this Entity.
     *
     * @return last message
     */
    public String getMessage() {
        return unwrap().getMessage();
    }

    /**
     * Get the model of this Entity
     *
     * @return model
     */
    public Model getModel() {
        Model model = unwrap().getModel();
        if (model == null || !model.isValid()) {
            return null;
        }
        return model.update(ctx, getLocalX(), getLocalY(), getOrientation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLocalX() {
        return unwrap().getLocalX();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLocalY() {
        return unwrap().getLocalY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getX() {
        return ctx.client.getBaseX() + (getLocalX() >> Constants.LOCAL_XY_SHIFT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getY() {
        return ctx.client.getBaseY() + (getLocalY() >> Constants.LOCAL_XY_SHIFT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getZ() {
        return ctx.client.getPlane();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBasePoint() {
        return ctx.viewport.convert(getLocalX(), getLocalY(), (unwrap().getHeight() / 2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getClickPoint() {
        if (getModel() != null) {
            return getModel().getClickPoint();
        }
        return getBasePoint();
    }

    @Override
    public boolean isValid() {
        return unwrap() != null;
    }

    /**
     * Checks if the entity is engaged in combat
     *
     * @return true if the current entity is in combat
     */
    public boolean isInCombat() {
        return ctx.client.getLoopCycle() < getLoopCycleStatus();
    }

    /**
     * Checks if the entity is currently moving
     *
     * @return true if the entity is moving
     */
    public boolean isMoving() {
        return getQueueSize() != 0;
    }

    /**
     * Checks if the entity is interacting with other
     * entities
     *
     * @return true if it is
     */
    public boolean isInteracting() {
        return getInteractingEntity() != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity) {
            return ((Entity) obj).unwrap().equals(unwrap());
        }
        return super.equals(obj);
    }

    /**
     * Returns the entities name
     */
    public abstract String getName();
}
