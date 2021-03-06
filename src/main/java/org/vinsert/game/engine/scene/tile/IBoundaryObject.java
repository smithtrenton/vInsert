package org.vinsert.game.engine.scene.tile;

import org.vinsert.game.engine.renderable.IRenderable;

/**
 * @author const_
 */
public interface IBoundaryObject extends IGameObject {


    IRenderable getRenderable();


    int getHash();


    int getX();


    int getY();


    int getPlane();


    int getOrientation();
}
