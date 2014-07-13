package org.vinsert.api.wrappers;

import org.vinsert.game.engine.cache.composite.IObjectComposite;

import java.lang.ref.WeakReference;

/**
 * Wrapper for IObjectComposite
 */
public final class ObjectComposite {
    private final WeakReference<IObjectComposite> wrapped;

    public ObjectComposite(IObjectComposite wrapped) {
        this.wrapped = new WeakReference<>(wrapped);
    }

    public IObjectComposite unwrap() {
        return wrapped.get();
    }

    public short[] getOriginalModelColors() {
        return unwrap().getOriginalModelColors();
    }

    public String[] getActions() {
        return unwrap().getActions();
    }


    public String getName() {
        return unwrap().getName();
    }


    public int getSizeX() {
        return unwrap().getSizeX();
    }


    public int getSizeY() {
        return unwrap().getSizeY();
    }


    public boolean isSolid() {
        return unwrap().isSolid();
    }


    public boolean isWalkable() {
        return unwrap().isWalkable();
    }


    public boolean isDelayShading() {
        return unwrap().isDelayShading();
    }


    public boolean getABoolean() {
        return unwrap().getABoolean();
    }


    public int getAnimation() {
        return unwrap().getAnimation();
    }


    public int getLightAmbient() {
        return unwrap().getLightAmbient();
    }


    public int getLightDiffuse() {
        return unwrap().getLightDiffuse();
    }


    public short[] getModifiedModelColors() {
        return unwrap().getModifiedModelColors();
    }


    public int getIcon() {
        return unwrap().getIcon();
    }


    public boolean isRotated() {
        return unwrap().isRotated();
    }


    public boolean isCastingShadow() {
        return unwrap().isCastingShadow();
    }


    public int getModelSizeX() {
        return unwrap().getModelSizeX();
    }


    public int getModelSizeY() {
        return unwrap().getModelSizeY();
    }


    public int getModelSizeZ() {
        return unwrap().getModelSizeZ();
    }


    public int getMapScene() {
        return unwrap().getMapScene();
    }


    public int getTranslateX() {
        return unwrap().getTranslateX();
    }


    public int getTranslateY() {
        return unwrap().getTranslateY();
    }


    public int getTranslateZ() {
        return unwrap().getTranslateZ();
    }


    public boolean getUnknown() {
        return unwrap().getUnknown();
    }


    public boolean isUnwalkableSolid() {
        return unwrap().isUnwalkableSolid();
    }


    public int getSolidInt() {
        return unwrap().getSolidInt();
    }
}
