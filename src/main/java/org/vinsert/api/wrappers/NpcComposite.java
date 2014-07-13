package org.vinsert.api.wrappers;

import org.vinsert.game.engine.cache.composite.INpcComposite;

import java.lang.ref.WeakReference;

/**
 * Wrapper for INpcComposite
 *
 * @author const_
 */
public final class NpcComposite implements Wrapper<INpcComposite> {
    private final WeakReference<INpcComposite> wrapped;

    public NpcComposite(INpcComposite wrapped) {
        this.wrapped = new WeakReference<>(wrapped);
    }


    public String getName() {
        return unwrap().getName();
    }


    public String[] getActions() {
        return unwrap().getActions();
    }


    public int[] getModelIds() {
        return unwrap().getModelIds();
    }


    public int getBoundaryDimension() {
        return unwrap().getBoundaryDimension();
    }


    public int getStandAnimationId() {
        return unwrap().getStandAnimationId();
    }


    public int getWalkAnimationId() {
        return unwrap().getWalkAnimationId();
    }


    public int getTurnAroundAnimationId() {
        return unwrap().getTurnAroundAnimationId();
    }


    public int getTurnRightAnimationId() {
        return unwrap().getTurnRightAnimationId();
    }


    public int getTurnLeftAnimationId() {
        return unwrap().getTurnLeftAnimationId();
    }


    public short[] getModifiedModelColors() {
        return unwrap().getModifiedModelColors();
    }

    public short[] getOriginalModelColors() {
        return unwrap().getOriginalModelColors();
    }


    public int[] getHeadModelIndexes() {
        return unwrap().getHeadModelIndexes();
    }


    public boolean isShownOnMap() {
        return unwrap().isShownOnMap();
    }


    public int getCombatLevel() {
        return unwrap().getCombatLevel();
    }


    public int getSizeX() {
        return unwrap().getSizeX();
    }


    public int getSizeY() {
        return unwrap().getSizeY();
    }


    public boolean isVisible() {
        return unwrap().isVisible();
    }


    public int getBrightness() {
        return unwrap().getBrightness();
    }


    public int getContrast() {
        return unwrap().getContrast();
    }


    public int getHeadIcon() {
        return unwrap().getHeadIcon();
    }


    public int getInitialRotation() {
        return unwrap().getInitialRotation();
    }


    public int getVarBitId() {
        return unwrap().getVarBitId();
    }


    public boolean isInteractive() {
        return unwrap().isInteractive();
    }


    public int getId() {
        return unwrap().getId();
    }

    @Override
    public INpcComposite unwrap() {
        return wrapped.get();
    }

    public boolean isValid() {
        INpcComposite raw = unwrap();
        return raw != null && raw.getName() != null
                && raw.getName().length() > 0
                && !raw.getName().equals("null");
    }
}
