package org.vinsert.game.engine.cache.composite;

/**
 * @author const_
 */
public interface INpcComposite {

    String getName();

    String[] getActions();

    int[] getModelIds();

    int getBoundaryDimension();

    int getStandAnimationId();

    int getWalkAnimationId();

    int getTurnAroundAnimationId();

    int getTurnRightAnimationId();

    int getTurnLeftAnimationId();

    short[] getModifiedModelColors();

    int getRealId();

    int[] getTransformIds();

    short[] getOriginalModelColors();

    int[] getHeadModelIndexes();

    boolean isShownOnMap();

    int getCombatLevel();

    int getSizeX();

    int getSizeY();

    boolean isVisible();

    int getBrightness();

    int getContrast();

    int getHeadIcon();

    int getInitialRotation();

    int getVarBitId();

    boolean isInteractive();

    int getId();

    int getSettingId();

}
