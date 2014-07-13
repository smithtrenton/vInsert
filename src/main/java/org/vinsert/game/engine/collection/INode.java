package org.vinsert.game.engine.collection;

/**
 * Accessor for the Node class.
 */
public interface INode {

    INode getNext();

    INode getPrev();

    long getUid();

}
