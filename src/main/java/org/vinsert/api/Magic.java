package org.vinsert.api;

import org.vinsert.api.wrappers.Spell;

/**
 * @author const_
 */
public interface Magic {

    /**
     * Casts the spell specified.
     *
     * @param spell spell to be cast.
     * @return <b>true</b> if successful otherwise <b>false</b>
     */
    boolean cast(Spell spell);

}
