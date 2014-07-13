package org.vinsert.api.wrappers.interaction;

import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.Model;
import org.vinsert.api.wrappers.Tile;

/**
 * A SceneNode is a part of the RuneScape scene graph.
 * Examples of scene nodes are:
 * - NPCs
 * - Players
 * - Objects
 */
public abstract class SceneNode extends Interactable {

    public SceneNode(MethodContext context) {
        super(context);
    }

    /**
     * Gets the local X coordinate.
     * This coordinate is only valid within the
     * currently loaded region.
     *
     * @return local X
     */
    public abstract int getLocalX();

    /**
     * Gets the local Y coordinate.
     * This coordinate is only valid within the
     * currently loaded region.
     *
     * @return local Y
     */
    public abstract int getLocalY();

    /**
     * Gets the global X coordinate.
     *
     * @return global X
     */
    public abstract int getX();

    /**
     * Gets the global Y coordinate.
     *
     * @return global Y
     */
    public abstract int getY();

    /**
     * Gets the global Z (plane) coordinate.
     *
     * @return global Z
     */
    public abstract int getZ();

    /**
     * Gets the global position of this SceneNode
     *
     * @return tile
     */
    public Tile getTile() {
        return new Tile(getX(), getY(), getZ());
    }

    /**
     * Gets the distance to another tile
     *
     * @param node SceneNode to find the distance to
     * @return The distance between this and the SceneNode specified
     */
    public int distanceTo(SceneNode node) {
        return getTile().distanceTo(node.getTile());
    }

    public int distanceTo(Tile tile) {
        return tile.distanceTo(getTile());
    }

    /**
     * Gets the model of the node
     *
     * @return the model
     */
    public abstract Model getModel();

    @Override
    public Result interact(String menuOption) {
        if (!isOnScreen() && distanceTo(context.players.getLocal()) < 5) {
            context.camera.turnTo(this);
        }
        return super.interact(menuOption);
    }

    @Override
    public void hover() {
        if (!isOnScreen() && distanceTo(context.players.getLocal()) < 5) {
            context.camera.turnTo(this);
        }
        super.hover();
    }

    @Override
    public void setAllowed(boolean allowed) {
        if (getModel() != null) {
            getModel().setAllowed(allowed);
        }
    }

    @Override
    public boolean isOnScreen() {
        Model model = getModel();
        if (model == null) {
            return super.isOnScreen();
        }
        return model.isOnScreen();
    }
}
