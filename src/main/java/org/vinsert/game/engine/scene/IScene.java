package org.vinsert.game.engine.scene;

import org.vinsert.game.engine.scene.tile.ISceneTile;

/**
 * @author tobiewarburton
 */
public interface IScene {


    ISceneTile[][][] getTiles();


    void renderObjects(ISceneTile tile, boolean bool);
}
