package org.vinsert.game.engine.media;

/**
 * @author const_
 */
public interface IWidget {

    int getScrollX();

    int getScrollY();

    int getUid();

    IWidget[] getChildren();

    int getLoopCycleStatus();

    int getType();

    int getModelType();

    int getModelId();

    String getSelectedAction();

    String[] getActions();

    int getOffsetX();

    int getOffsetY();

    int getWidth();

    int getHeight();

    int getParentId();

    String getText();

    int getScrollMaxHorizontal();

    int getMaxScrollVertical();

    int getModelZoom();

    int getRotationX();

    int getRotationY();

    int getRotationZ();

    String getComponentName();

    int getItemId();

    int getItemStackSize();

    int getComponentIndex();

    int getTextColor();

    int getTextureId();

    int getFontId();

    int getBorderThickness();

    int getX();

    int getY();

    int getBoundsIndex();

    int[] getInventory();

    int[] getInventoryStackSizes();

}
