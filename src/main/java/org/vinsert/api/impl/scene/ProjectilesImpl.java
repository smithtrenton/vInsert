package org.vinsert.api.impl.scene;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Projectiles;
import org.vinsert.api.wrappers.Deque;
import org.vinsert.api.wrappers.Projectile;
import org.vinsert.game.engine.collection.IDeque;
import org.vinsert.game.engine.renderable.IProjectile;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * API implementation for accessing projectiles
 *
 * @author const_
 * @see org.vinsert.api.Projectiles
 */
public final class ProjectilesImpl implements Projectiles {

    private final MethodContext ctx;

    @Inject
    public ProjectilesImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<Projectile> getAll() {
        List<Projectile> list = newArrayList();
        IDeque deque = ctx.client.getProjectiles();
        if (deque != null) {
            Deque wrappedDeque = new Deque(deque);
            while (wrappedDeque.hasNext()) {
                list.add(new Projectile(ctx, (IProjectile) wrappedDeque.next()));
            }
        }
        return list;
    }
}
