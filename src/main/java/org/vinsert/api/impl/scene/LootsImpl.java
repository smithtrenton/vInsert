package org.vinsert.api.impl.scene;

import com.google.inject.Inject;
import org.vinsert.api.Loots;
import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.queries.LootQuery;
import org.vinsert.api.wrappers.Deque;
import org.vinsert.api.wrappers.GroundLayer;
import org.vinsert.api.wrappers.Loot;
import org.vinsert.game.engine.collection.IDeque;
import org.vinsert.game.engine.renderable.ILoot;
import org.vinsert.game.engine.scene.tile.IGroundLayer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * API implementation for accessing loot information
 *
 * @author const_
 * @see org.vinsert.api.Loots
 */
public final class LootsImpl implements Loots {

    private final MethodContext ctx;

    @Inject
    public LootsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loot> getAll() {
        List<Loot> items = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                items.addAll(getLootAt(x, y));
            }
        }
        return items;
    }

    private List<Loot> getLootAt(int x, int y) {
        List<Loot> items = newArrayList();
        if (ctx.client.getScene() != null && ctx.client.getScene().getTiles()[ctx.client.getPlane()][x][y] != null) {
            int plane = ctx.client.getPlane();
            IDeque itemsDeque = ctx.client.getLoot()[plane][x][y];
            IGroundLayer layer = ctx.client.getScene().getTiles()[plane][x][y].getGroundLayer();
            if (itemsDeque != null) {
                Deque deque = new Deque(itemsDeque);
                while (deque.hasNext()) {
                    ILoot item = (ILoot) deque.next();
                    GroundLayer gLayer = new GroundLayer(layer);
                    if (item != null) {
                        items.add(new Loot(ctx, item, gLayer, x, y, plane));
                    }
                }
            }
        }
        return items;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LootQuery find() {
        return new LootQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LootQuery find(String... names) {
        return new LootQuery(ctx).named(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LootQuery find(int... ids) {
        return new LootQuery(ctx).id(ids);
    }
}
