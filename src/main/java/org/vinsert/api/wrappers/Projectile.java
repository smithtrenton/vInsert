package org.vinsert.api.wrappers;

import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.interaction.SceneNode;
import org.vinsert.game.engine.renderable.IProjectile;

import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * @author : const_
 */
public final class Projectile extends SceneNode {

    private final WeakReference<IProjectile> wrapped;

    public Projectile(MethodContext ctx, IProjectile projectile) {
        super(ctx);
        this.wrapped = new WeakReference<>(projectile);
    }

    @Override
    public int getLocalX() {
        return unwrap().getX();
    }

    @Override
    public int getLocalY() {
        return unwrap().getY();
    }

    @Override
    public int getX() {
        return unwrap().getX() + context.client.getBaseX();
    }

    @Override
    public int getY() {
        return unwrap().getY() + context.client.getBaseY();
    }

    @Override
    public int getZ() {
        return unwrap().getZ();
    }

    public int getVelocity() {
        return unwrap().getVelocity();
    }

    @Override
    public Model getModel() {
        return unwrap().getModel();
    }

    @Override
    public Point getBasePoint() {
        return context.viewport.convert(getX(), getY(), getZ());
    }

    @Override
    public Point getClickPoint() {
        if (getModel() != null) {
            return getModel().getClickPoint();
        }
        return getBasePoint();
    }

    @Override
    public boolean isValid() {
        return getModel() != null;
    }

    public IProjectile unwrap() {
        return wrapped.get();
    }
}
