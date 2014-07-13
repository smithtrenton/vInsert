package org.vinsert.game.engine.renderable.entity;

import org.vinsert.game.engine.renderable.IRenderable;

/**
 * @author tobiewarburton
 */
public interface IEntity extends IRenderable {

    String getMessage();

    int getLocalX();

    int getLocalY();

    int[] getQueueX();

    int[] getQueueY();

    int getAnimationId();

    int getOrientation();

    int getCurrentHealth();

    int getMaxHealth();

    int getInteractingEntity();

    int getQueueSize();

    int getLoopCycleStatus();
}
