package org.vinsert.game.engine.net;

import org.vinsert.game.engine.collection.INode;

/**
 *
 */
public interface IBuffer extends INode {

    byte[] getPayload();

    int getOffset();

}
