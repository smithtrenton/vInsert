package org.vinsert.api.wrappers;

import org.vinsert.api.Constants;
import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.interaction.SceneNode;
import org.vinsert.game.engine.cache.media.IModel;
import org.vinsert.game.engine.renderable.IRenderable;
import org.vinsert.game.engine.scene.tile.IGameObject;

import java.awt.*;
import java.lang.ref.WeakReference;

/**
 *
 */
public final class GameObject extends SceneNode implements Wrapper<IGameObject> {
    private final WeakReference<IGameObject> wrapped;
    private final GameObjectType type;
    private ObjectComposite composite;

    public GameObject(MethodContext context, IGameObject wrapped, GameObjectType type) {
        super(context);
        this.wrapped = new WeakReference<>(wrapped);
        this.type = type;
    }

    /**
     * Gets the ID of the object.
     *
     * @return id
     */
    public int getId() {
        return unwrap() != null ? unwrap().getHash() >> 14 & 32767 : -1;
    }

    /**
     * Gets the type of object
     *
     * @return type
     */
    public GameObjectType getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    public int getLocalX() {
        return unwrap().getX();
    }

    /**
     * {@inheritDoc}
     */
    public int getLocalY() {
        return unwrap().getY();
    }

    /**
     * {@inheritDoc}
     */
    public int getX() {
        return (getLocalX() >> Constants.LOCAL_XY_SHIFT) + context.client.getBaseX();
    }

    /**
     * {@inheritDoc}
     */
    public int getY() {
        return (getLocalY() >> Constants.LOCAL_XY_SHIFT) + context.client.getBaseY();
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
    public Model getModel() {
        IRenderable renderable = unwrap().getRenderable();
        if (renderable instanceof IModel) {
            return new Model(context, (IModel) renderable, getLocalX(), getLocalY(),
                    0);
        } else if (renderable != null && renderable.getModel() != null) {
            return renderable.getModel().update(context, getLocalX(), getLocalY(), 0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBasePoint() {
        return context.viewport.convert(getTile());
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
        return unwrap() != null && context.objects.find().location(
                getTile()).id(getId()).exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGameObject unwrap() {
        return wrapped.get();
    }

    /**
     * Gets the objects name
     *
     * @return the objects name
     */
    public String getName() {
        return getComposite() != null ? getComposite().getName() : null;
    }

    /**
     * Gets the object composite for this objects id
     */
    public ObjectComposite getComposite() {
        if (composite != null) {
            return composite;
        }
        ObjectComposite composite = context.composites.getObjectComposite(getId());
        if (composite == null) {
            return null;
        }
        return (this.composite = composite);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameObject) {
            return ((GameObject) obj).unwrap().equals(wrapped);
        }
        return super.equals(obj);
    }

    /**
     * An enum defining several types of objects.
     */
    public static enum GameObjectType {
        FLOOR, WALL, INTERACTABLE, BOUNDARY
    }
}
