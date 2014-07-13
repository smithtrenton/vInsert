package org.vinsert.api.impl.tabs;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Prayers;
import org.vinsert.api.wrappers.Prayer;
import org.vinsert.api.wrappers.Skill;
import org.vinsert.api.wrappers.Tab;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.interaction.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Prayers interaction implementation
 *
 * @author tommo
 */
public final class PrayersImpl implements Prayers {

    private static final int GROUP_ID = 271;
    private static final int PRAYER_POINTS_WIDGET = 1;

    private final MethodContext ctx;

    @Inject
    public PrayersImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getPrayerPoints() {
        Widget widget = ctx.widgets.find(GROUP_ID, PRAYER_POINTS_WIDGET).single();
        return Integer.parseInt(widget.getText().split("/")[0].trim());
    }

    @Override
    public List<Prayer> getActivatedPrayers() {
        List<Prayer> list = new ArrayList<Prayer>();
        for (Prayer prayer : Prayer.values()) {
            if (isActivated(prayer)) {
                list.add(prayer);
            }
        }
        return list;
    }

    @Override
    public boolean isActivated(Prayer prayer) {
        return ctx.settings.getWidgetSetting(prayer.getSettingsIndex()) == 1;
    }

    @Override
    public boolean canActivate(Prayer prayer) {
        return ctx.skills.getBaseLevel(Skill.PRAYER) >= prayer.getRequiredLevel();
    }

    @Override
    public boolean activate(Prayer prayer) {
        if (isActivated(prayer)) {
            return true;
        }

        if (!ctx.tabs.isOpen(Tab.PRAYER)) {
            ctx.tabs.open(Tab.PRAYER);
        }

        Widget widget = ctx.widgets.find(GROUP_ID, prayer.getWidgetId()).single();
        Result result = widget.interact("Activate");
        return result.equals(Result.OK);
    }

    @Override
    public boolean deactivate(Prayer prayer) {
        if (!isActivated(prayer)) {
            return true;
        }

        if (!ctx.tabs.isOpen(Tab.PRAYER)) {
            ctx.tabs.open(Tab.PRAYER);
        }

        Widget widget = ctx.widgets.find(GROUP_ID, prayer.getWidgetId()).single();
        Result result = widget.interact("Deactivate");
        return result.equals(Result.OK);
    }
}
