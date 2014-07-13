package org.vinsert.game.engine.cache.composite;

/**
 * @author const_
 */
public interface IObjectComposite {

    IObjectComposite getObjectComposite(int id, int opaque);

    String[] getActions();

    short[] getOriginalModelColors();

    String getName();

    int getSizeX();

    int getSizeY();

    boolean isSolid();

    boolean isWalkable();

    boolean isDelayShading();

    boolean getABoolean();

    int getAnimation();

    int getLightAmbient();

    int getLightDiffuse();

    short[] getModifiedModelColors();

    int getIcon();

    boolean isRotated();

    boolean isCastingShadow();

    int getModelSizeX();

    int getModelSizeY();

    int getModelSizeZ();

    int getMapScene();

    int getTranslateX();

    int getTranslateY();

    int getTranslateZ();

    boolean getUnknown();

    boolean isUnwalkableSolid();

    int getSolidInt();

}
