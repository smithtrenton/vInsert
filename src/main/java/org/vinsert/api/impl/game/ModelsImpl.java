package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Models;
import org.vinsert.api.wrappers.Model;
import org.vinsert.api.wrappers.Npc;
import org.vinsert.game.engine.cache.media.IModel;
import org.vinsert.game.engine.collection.ICache;
import org.vinsert.game.engine.collection.INode;

/**
 * API implementation for retrieving model objects from caches.
 *
 * @author const_
 * @see org.vinsert.api.Models
 */
public final class ModelsImpl implements Models {

    private final MethodContext context;

    @Inject
    public ModelsImpl(MethodContext context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model getNpcModel(Npc npc) {
        ICache cache = context.client.getNpcModelCache();
        for (INode chain : cache.getBag().getCache()) {
            for (INode in = chain.getNext(); in != null && !in.equals(chain); in = in.getNext()) {
                if (in.getUid() == npc.getId()) {
                    return new Model(context, (IModel) in,
                            npc.getLocalX(), npc.getLocalY(), npc.getOrientation());
                }
            }
        }
        return null;
    }
}
