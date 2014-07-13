package org.vinsert.api.wrappers;

import org.vinsert.game.engine.cache.composite.IItemComposite;

import java.lang.ref.WeakReference;

/**
 * Wrapper for item composites.
 */
public class ItemComposite implements Wrapper<IItemComposite> {
    private final WeakReference<IItemComposite> wrapped;

    public ItemComposite(IItemComposite wrapped) {
        this.wrapped = new WeakReference<>(wrapped);
    }

    public String getName() {
        return unwrap().getName();
    }


    public String[] getInventoryActions() {
        return unwrap().getInventoryActions();
    }


    public String[] getGroundActions() {
        return unwrap().getGroundActions();
    }


    public int getModelId() {
        return unwrap().getModelId();
    }


    public int getModelZoom() {
        return unwrap().getModelZoom();
    }


    public int getModelRotation1() {
        return unwrap().getModelRotation1();
    }


    public int getModelRotation2() {
        return unwrap().getModelRotation2();
    }


    public int getModelOffset1() {
        return unwrap().getModelOffset1();
    }


    public int getSine() {
        return unwrap().getSine();
    }


    public int getMaleEquipPrimaryModel() {
        return unwrap().getMaleEquipPrimaryModel();
    }


    public int getMaleEquipOffset() {
        return unwrap().getMaleEquipOffset();
    }


    public int getMaleEquipSecondaryModel() {
        return unwrap().getMaleEquipSecondaryModel();
    }


    public int getFemaleEquipPrimaryModel() {
        return unwrap().getFemaleEquipPrimaryModel();
    }


    public int getFemaleEquipOffset() {
        return unwrap().getFemaleEquipOffset();
    }


    public int getFemaleEquipSecondaryModel() {
        return unwrap().getFemaleEquipSecondaryModel();
    }


    public int getMaleEmblem() {
        return unwrap().getMaleEmblem();
    }


    public int getFemaleEmblem() {
        return unwrap().getFemaleEmblem();
    }


    public int getMaleDialogueHat() {
        return unwrap().getMaleDialogueHat();
    }


    public int getFemaleDialogueHat() {
        return unwrap().getFemaleDialogueHat();
    }


    public int getDiagonalRotation() {
        return unwrap().getDiagonalRotation();
    }


    public int getNoteIndex() {
        return unwrap().getNoteIndex();
    }


    public int getModelSizeY() {
        return unwrap().getModelSizeY();
    }


    public int getLightModifier() {
        return unwrap().getLightModifier();
    }


    public int getShadowModifier() {
        return unwrap().getShadowModifier();
    }


    public int getTeamIndex() {
        return unwrap().getTeamIndex();
    }

    @Override
    public IItemComposite unwrap() {
        return wrapped.get();
    }
}
