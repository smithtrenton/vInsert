package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.Composites;
import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.ItemComposite;
import org.vinsert.api.wrappers.NpcComposite;
import org.vinsert.api.wrappers.ObjectComposite;
import org.vinsert.game.engine.cache.composite.IItemComposite;
import org.vinsert.game.engine.cache.composite.INpcComposite;
import org.vinsert.game.engine.cache.composite.IObjectComposite;
import org.vinsert.game.engine.collection.ICache;
import org.vinsert.game.engine.collection.INode;

import java.util.HashMap;
import java.util.Map;

/**
 * API implementation for dealing with composite caches.
 * TODO: Figure out what the hell const_ was doing in this class adding a Map.. what is it for!?
 *
 * @see org.vinsert.api.Composites
 */
public final class CompositesImpl implements Composites {
    private final MethodContext context;

    private static final Map<Integer, INpcComposite> NPC_MAP = new HashMap<>();

    @Inject
    public CompositesImpl(MethodContext context) {
        this.context = context;
    }

    @Override
    public ObjectComposite getObjectComposite(int id) {
        ICache compositeCache = context.client.getObjectCompositeCache();
        for (INode chain : compositeCache.getBag().getCache()) {
            for (INode in = chain.getNext(); in != null && !in.equals(chain); in = in.getNext()) {
                if (in.getUid() == id) {
                    return new ObjectComposite((IObjectComposite) in);
                }
            }
        }
        return null;
    }

    @Override
    public ItemComposite getItemComposite(int id) {
        ICache compositeCache = context.client.getItemCompositeCache();
        for (INode chain : compositeCache.getBag().getCache()) {
            for (INode in = chain.getNext(); in != null && !in.equals(chain); in = in.getNext()) {
                if (in.getUid() == id) {
                    return new ItemComposite((IItemComposite) in);
                }
            }
        }
        return null;
    }

    @Override
    public NpcComposite getNpcComposite(int id) {
        ICache compositeCache = context.client.getNpcCompositeCache();
        for (INode chain : compositeCache.getBag().getCache()) {
            for (INode in = chain.getNext(); in != null && !in.equals(chain); in = in.getNext()) {
                if (in.getUid() == id) {
                    return new NpcComposite((INpcComposite) in);
                }
            }
        }
        return null;
    }

    @Override
    public INpcComposite getNpcCompositeByName(String name) {
        for (INpcComposite composite : NPC_MAP.values()) {
            if (composite != null && composite.getName() != null &&
                    composite.getName().equalsIgnoreCase(name)) {
                return composite;
            }
        }
        return null;
    }

    @Override
    public INpcComposite getNpcCompositeById(int id) {
        if (!NPC_MAP.containsKey(id)) {
            for (INpcComposite npcComposite : NPC_MAP.values()) {
                if (npcComposite.getId() == id) {
                    return npcComposite;
                }
            }
        }
        return NPC_MAP.get(id);
    }

    public static void put(INpcComposite composite, int id) {
        NPC_MAP.put(id, composite);
    }

}
