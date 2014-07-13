package org.vinsert.game.engine.cache.composite;

/**
 * @author const_
 */
public interface IItemComposite {

    String getName();

    String[] getInventoryActions();

    String[] getGroundActions();

    int getModelId();

    int getModelZoom();

    int getModelRotation1();

    int getModelRotation2();

    int getModelOffset1();

    int getSine();

    int getMaleEquipPrimaryModel();

    int getMaleEquipOffset();

    int getMaleEquipSecondaryModel();

    int getFemaleEquipPrimaryModel();

    int getFemaleEquipOffset();

    int getFemaleEquipSecondaryModel();

    int getMaleEmblem();

    int getFemaleEmblem();

    int getMaleDialogueHat();

    int getFemaleDialogueHat();

    int getDiagonalRotation();

    int getNoteIndex();

    int getModelSizeY();

    int getLightModifier();

    int getShadowModifier();

    int getTeamIndex();

}
