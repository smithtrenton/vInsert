package org.vinsert.api.collection.queries;

import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.Filter;
import org.vinsert.api.wrappers.Area;
import org.vinsert.api.wrappers.Tile;
import org.vinsert.api.wrappers.interaction.SceneNode;

/**
 * Base queries for SceneNodes
 *
 * @param <E> Actual type
 * @param <T> Actual query
 */
public abstract class SceneNodeQuery<E extends SceneNode, T extends SceneNodeQuery> extends Query<E> {
    protected final MethodContext context;

    public SceneNodeQuery(final MethodContext context) {
        this.context = context;
    }

    /**
     * Checks if the Scene Node is at the specified location.
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T location(final Tile tile) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return acceptable.getTile().equals(tile);
            }
        });
        return (T) this;
    }

    /**
     * Checks if the Scene Node is in the specified area
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T area(final Area area) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return area.contains(acceptable);
            }
        });
        return (T) this;
    }

    /**
     * Checks if the Scene Node is reachable
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T canReach() {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return context.walking.canReach(acceptable);
            }
        });
        return (T) this;
    }

    /**
     * Checks if the Scene node is on screen
     *
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T visible() {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return acceptable.isOnScreen();
            }
        });
        return (T) this;
    }

    /**
     * Checks if the SceneNode is within a certain distance from the player.
     *
     * @param distance distance to check
     * @return query
     */
    @SuppressWarnings("unchecked")
    public T distance(final int distance) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                return context.players.getLocal().getTile().distanceTo(acceptable.getTile()) < distance;
            }
        });
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T checksum(final String... checksums) {
        addCondition(new Filter<E>() {
            @Override
            public boolean accept(E acceptable) {
                if (acceptable.getModel() != null) {
                    for (String checksum : checksums) {
                        if (acceptable.getModel().getChecksum().equals(checksum)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T orderByDistance() {
        return (T) this;
    }

}
