package org.vinsert.api.impl.tabs;

import org.vinsert.api.MethodContext;
import org.vinsert.api.wrappers.Skill;
import org.vinsert.api.wrappers.Tab;
import org.vinsert.api.wrappers.Widget;

/**
 *
 */
public final class StatsImpl {
    private final MethodContext ctx;

    public StatsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    public void hover(Skill skill) {
        int widgetId = determineWidgetID(skill);
        if (widgetId != -1) {
            if (!ctx.tabs.isOpen(Tab.STATS)) {
                ctx.tabs.open(Tab.STATS);
            }

            Widget widget = ctx.widgets.find().id(320, widgetId).single();
            if (widget != null) {
                widget.hover();
            }
        }
    }

    private int determineWidgetID(Skill skill) {
        switch (skill) {

            case ATTACK:
                return 48;

            case HITPOINTS:
                return 49;

            case MINING:
                return 50;

            case STRENGTH:
                return 51;

            case AGILITY:
                return 52;

            case SMITHING:
                return 53;

            case DEFENCE:
                return 54;

            case HERBLORE:
                return 55;

            case FISHING:
                return 55;
        }
        return -1;
    }


}
