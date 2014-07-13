package org.vinsert.api.impl.tabs;

import com.google.inject.Inject;
import org.vinsert.api.Magic;
import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.Spell;
import org.vinsert.api.wrappers.Tab;
import org.vinsert.api.wrappers.Widget;

/**
 * @author const_
 */
public final class MagicImpl implements Magic {

    private static final int GROUP = 192;
    private final MethodContext ctx;

    @Inject
    public MagicImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    private Widget getButton(int id) {
        return ctx.widgets.find().id(GROUP, id).single();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cast(Spell spell) {
        if (!ctx.tabs.open(Tab.MAGIC)) {
            ctx.tabs.open(Tab.MAGIC);
        }
        Widget spellButton = getButton(spell.getId());
        if (spellButton != null) {
            spellButton.click(true);
            return true;
        }
        return false;
    }
}