package org.vinsert.game.engine.collection;

/**
 * Accessor for the Node class.
 */
public interface ICacheableNode extends INode {


    ICacheableNode getNext();

    ICacheableNode getPrev();

}
