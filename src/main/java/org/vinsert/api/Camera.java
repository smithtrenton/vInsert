package org.vinsert.api;

import org.vinsert.api.wrappers.Tile;
import org.vinsert.api.wrappers.interaction.SceneNode;

/**
 * @author const_
 */
public interface Camera {

    /**
     * Gets the camera X
     *
     * @return camera x
     */
    int getX();

    /**
     * Gets the camera y
     *
     * @return camera y
     */
    int getY();

    /**
     * Gets the camera z
     *
     * @return camera z
     */
    int getZ();


    /**
     * Retrieves the current camera angle.
     *
     * @return camera angle
     */
    int getAngle();

    /**
     * Retrieves the current camera pitch
     *
     * @return camera pitch
     */
    int getPitch();

    /**
     * Sets the camera to the specified value.
     *
     * @param pitch pitch of the height.
     */
    void setPitch(int pitch);

    /**
     * Sets the compass to specified face direction
     *
     * @param dir Direction
     *            'n' for North,
     *            'e' for East,
     *            's' for South,
     *            'w' for West
     */
    void setCompass(char dir);

    /**
     * Sets the camera to the specified angle
     *
     * @param degrees degrees of the turn
     */
    void setAngle(int degrees);

    /**
     * Moves the camera view to the extreme up or down.
     *
     * @param up true move camera up, false down.
     */
    void setPitch(boolean up);

    /**
     * Calculates the angle to tile
     *
     * @param tile the tile in which you want to get the angle to
     * @return the angle to tile
     */
    int getAngleTo(Tile tile);

    /**
     * Turns to the given Locatable with the given deviation.
     *
     * @param node      scenenode to turn to.
     * @param deviation degree of accuracy.
     */
    void turnTo(SceneNode node, int deviation);

    /**
     * Turns to the given Locatable.
     *
     * @param node scenenode to turn to.
     */
    void turnTo(SceneNode node);

    /**
     * Sets the camera to the specified value using the mouse.
     *
     * @param pitch pitch of the height.
     */
    void setPitchM(int pitch);


    /**
     * Sets the camera to the specified angle using the mouse
     *
     * @param degrees degrees of the turn
     */
    void setAngleM(int degrees);

    /**
     * Moves the camera view to the extreme up or down using the mouse.
     *
     * @param up true move camera up, false down.
     */
    void setPitchM(boolean up);

    /**
     * Turns to the given Locatable with the given deviation using the mouse.
     *
     * @param node      scenenode to turn to.
     * @param deviation degree of accuracy.
     */
    void turnToM(SceneNode node, int deviation);

    /**
     * Turns to the given Locatable using the mouse.
     *
     * @param node scenenode to turn to.
     */
    void turnToM(SceneNode node);

}
