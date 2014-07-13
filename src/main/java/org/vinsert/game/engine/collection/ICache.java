package org.vinsert.game.engine.collection;

/**
 * @author tobiewarburton
 */
public interface ICache {

    IBag getBag();

    IQueue getQueue();

    ICacheableNode getEmptyCacheableNode();

}
