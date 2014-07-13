package org.vinsert.api.wrappers;

import org.vinsert.game.engine.renderable.IRenderable;
import org.vinsert.game.engine.scene.tile.IGroundLayer;

import java.lang.ref.WeakReference;


/**
 * @author : const_
 */
public final class GroundLayer {

    private WeakReference<IGroundLayer> wrapped;

    public GroundLayer(IGroundLayer wrapped) {
        this.wrapped = new WeakReference<>(wrapped);
    }

    public IGroundLayer unwrap() {
        return wrapped.get();
    }

    public IRenderable getRenderable(int layer) {
        IRenderable renderable = null;
        switch (layer) {
            case 1:
                renderable = unwrap().getLayer1();
                break;
            case 2:
                renderable = unwrap().getLayer2();
                break;
            case 3:
                renderable = unwrap().getLayer3();
                break;
        }
        return renderable;
    }
}
