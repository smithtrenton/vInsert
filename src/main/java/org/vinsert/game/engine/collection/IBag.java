package org.vinsert.game.engine.collection;

/**
 * Accessor for the Bag class.
 *
 * @author tobiewarburton
 */
public interface IBag {

    INode[] getCache();

    int getSize();

    INode get(long id);

    int getCurrentIndex();

    INode getCurrent();

    INode getLastRetrieved();

}
