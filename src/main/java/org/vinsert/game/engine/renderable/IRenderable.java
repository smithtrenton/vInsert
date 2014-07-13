package org.vinsert.game.engine.renderable;

import org.vinsert.api.wrappers.Model;
import org.vinsert.game.engine.collection.ICacheableNode;

/**
 *
 */
public interface IRenderable extends ICacheableNode {


    int getHeight();


    Model getModel();
}
