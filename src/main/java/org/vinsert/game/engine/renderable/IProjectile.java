package org.vinsert.game.engine.renderable;

/**
 * @author const_
 */
public interface IProjectile extends IRenderable {


    int getX();


    int getY();


    int getZ();


    int getXVelocity();


    int getYVelocity();


    int getZVelocity();


    int getVelocity();


    void setSpeed(int speed);
}
