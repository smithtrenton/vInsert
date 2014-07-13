package org.vinsert.api.wrappers;

import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.interaction.SceneNode;
import org.vinsert.game.engine.renderable.ILoot;
import org.vinsert.game.engine.renderable.IRenderable;

import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * @author const_
 */
public final class Loot extends SceneNode implements Wrapper<ILoot> {

    private final WeakReference<ILoot> wrapped;
    private final int localX;
    private final int localY;
    private final GroundLayer layer;
    private ItemComposite composite;

    public Loot(MethodContext ctx, ILoot wrapped, GroundLayer layer, int localX, int localY, int z) {
        super(ctx);
        this.wrapped = new WeakReference<>(wrapped);
        this.localX = localX;
        this.localY = localY;
        this.layer = layer;
    }

    /**
     * Gets the id of the ground item
     *
     * @return the id of the ground item
     */
    public int getId() {
        return unwrap().getId();
    }

    /**
     * Gets the stack size of the ground item
     *
     * @return the stack size of the ground item
     */
    public int getStackSize() {
        return unwrap().getStackSize();
    }

    /**
     * Gets the items name
     *
     * @return the items name
     */
    public String getName() {
        return getComposite() != null ? getComposite().getName() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLocalX() {
        return localX * 128 + 64;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLocalY() {
        return localY * 128 + 64;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getX() {
        return localX + this.context.client.getBaseX();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getY() {
        return localY + this.context.client.getBaseY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getZ() {
        return context.client.getPlane();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBasePoint() {
        return this.context.viewport.convert(getLocalX(), getLocalY(), 0);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ILoot unwrap() {
        return wrapped.get();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Loot) {
            return ((Loot) obj).wrapped.equals(wrapped);
        }
        return super.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model getModel() {
        for (int i = 1; i < 4; i++) {
            IRenderable renderable = layer.getRenderable(i);
            if (renderable != null && renderable.getModel() != null
                    && renderable.getModel().isValid()) {
                return renderable.getModel().update(context, getLocalX(), getLocalY(), 0);
            }
        }
        return null;
    }

    /**
     * Gets the item composite for this items id
     */
    public ItemComposite getComposite() {
        if (composite != null) {
            return composite;
        }
        ItemComposite composite = context.composites.getItemComposite(getId());
        if (composite == null) {
            return null;
        }
        return (this.composite = composite);
    }

    @Override
    public boolean isValid() {
        return unwrap() != null;
    }
}
