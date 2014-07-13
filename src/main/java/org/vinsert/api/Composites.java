package org.vinsert.api;

import org.vinsert.api.wrappers.ItemComposite;
import org.vinsert.api.wrappers.NpcComposite;
import org.vinsert.api.wrappers.ObjectComposite;
import org.vinsert.game.engine.cache.composite.INpcComposite;

/**
 *
 */
public interface Composites {

    ObjectComposite getObjectComposite(int id);

    ItemComposite getItemComposite(int id);

    NpcComposite getNpcComposite(int id);

    INpcComposite getNpcCompositeById(int id);

    INpcComposite getNpcCompositeByName(String name);

}
