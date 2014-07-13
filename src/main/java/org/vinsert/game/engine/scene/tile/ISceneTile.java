package org.vinsert.game.engine.scene.tile;

import org.vinsert.game.engine.collection.INode;

/**
 * @author tobiewarburton
 */
public interface ISceneTile extends INode {


    IGroundLayer getGroundLayer();


    IInteractableObject[] getInteractableObjects();


    IBoundaryObject getBoundaryObject();


    IWallObject getWallObject();


    IFloorObject getFloorObject();


    int getX();


    int getY();


    int getPlane();
}
