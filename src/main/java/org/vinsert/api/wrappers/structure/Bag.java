package org.vinsert.api.wrappers.structure;

import org.vinsert.api.wrappers.Wrapper;
import org.vinsert.game.engine.collection.IBag;
import org.vinsert.game.engine.collection.INode;

/**
 * Wrapper for a set of nodes (also known as Bag)
 */
public final class Bag<T extends INode> implements Wrapper<IBag> {
    private final IBag wrapped;
    private INode current;
    private int caret;

    public Bag(IBag cache) {
        this.wrapped = cache;
        caret = 0;
    }

    /**
     * Get the first node in the bag.
     *
     * @return head
     */
    public T getHead() {
        caret = 0;
        return getNext();
    }

    /**
     * Get the next node based on the current position in the bag.
     *
     * @return next
     */
    @SuppressWarnings("unchecked")
    public T getNext() {
        INode[] sentinels = wrapped.getCache();

        if (caret > 0 && sentinels[caret - 1] != current) {
            INode node = current;
            current = node.getPrev();
            return (T) node;
        }

        while (sentinels.length > caret) {
            INode node = sentinels[caret++].getPrev();
            if (sentinels[caret - 1] != node) {
                current = node.getPrev();
                return (T) node;
            }
        }
        return null;
    }

    /**
     * Get a node at a specific index in the bag
     *
     * @param index index of node
     * @return node or null
     */
    @SuppressWarnings("unchecked")
    public T getSentinel(int index) {
        if (index < getLength()) {
            return (T) wrapped.getCache()[index];
        }
        return null;
    }

    /**
     * The length of the array of nodes in this bag.
     *
     * @return array length
     */
    public int getLength() {
        return wrapped.getCache().length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IBag unwrap() {
        return wrapped;
    }
}

