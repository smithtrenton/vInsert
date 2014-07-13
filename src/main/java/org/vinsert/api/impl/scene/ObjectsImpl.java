package org.vinsert.api.impl.scene;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Objects;
import org.vinsert.api.collection.queries.GameObjectQuery;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.game.engine.IClient;
import org.vinsert.game.engine.scene.IScene;
import org.vinsert.game.engine.scene.tile.IInteractableObject;
import org.vinsert.game.engine.scene.tile.ISceneTile;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * API implementation for accessing in-game objects.
 *
 * @see org.vinsert.api.Objects
 */
public final class ObjectsImpl implements Objects {
    private final MethodContext ctx;

    @Inject
    public ObjectsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameObject> getAll() {
        List<GameObject> objects = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getObjectsAt(x, y));
            }
        }
        return objects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameObjectQuery find() {
        return new GameObjectQuery(ctx);
    }

    @Override
    public GameObjectQuery find(int... ids) {
        return find().id(ids);
    }

    @Override
    public GameObjectQuery find(String... names) {
        return find().named(names);
    }

    /**
     * Gets all the objects at specified x/y
     *
     * @param x local X coordinate
     * @param y local Y coordinate
     * @return collection of objects at the specified tile
     */
    private Collection<GameObject> getObjectsAt(int x, int y) {
        List<GameObject> objects = newArrayList();
        IClient client = ctx.client;
        IScene worldController = client.getScene();
        ISceneTile[][][] grounds = worldController.getTiles();
        ISceneTile ground = grounds[client.getPlane()][x][y];

        if (ground != null) {
            try {
                if (ground.getFloorObject() != null) {
                    objects.add(new GameObject(ctx, ground.getFloorObject(), GameObject.GameObjectType.FLOOR));
                }

                if (ground.getWallObject() != null) {
                    objects.add(new GameObject(ctx, ground.getWallObject(), GameObject.GameObjectType.WALL));
                }

                if (ground.getBoundaryObject() != null) {
                    objects.add(new GameObject(ctx, ground.getBoundaryObject(), GameObject.GameObjectType.BOUNDARY));
                }

                if (ground.getInteractableObjects() != null) {
                    for (IInteractableObject object : ground.getInteractableObjects()) {
                        if (object == null) {
                            continue;
                        }
                        GameObject wrapped = new GameObject(ctx, object, GameObject.GameObjectType.INTERACTABLE);
                        objects.add(wrapped);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objects;
    }

}
