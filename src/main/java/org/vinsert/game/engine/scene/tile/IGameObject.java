package org.vinsert.game.engine.scene.tile;

import org.vinsert.game.engine.renderable.IRenderable;

/**
 *
 */
public interface IGameObject {

    int getHash();

    int getX();

    int getY();

    int getZ();

    int getOrientation();

    IRenderable getRenderable();

}
