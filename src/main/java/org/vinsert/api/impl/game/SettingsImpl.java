package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Settings;

/**
 * API implementation for dealing with a variety of in-game settings.
 *
 * @author const_
 * @see org.vinsert.api.Settings
 */
public final class SettingsImpl implements Settings {

    private final MethodContext ctx;

    @Inject
    public SettingsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getWidgetSettings() {
        int[] widgetSettings = ctx.client.getWidgetSettings();
        return widgetSettings != null ? widgetSettings : new int[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidgetSetting(int index) {
        int[] widgetSettings = getWidgetSettings();
        return widgetSettings != null && index < widgetSettings.length ? widgetSettings[index] : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getPlayerSettings() {
        int[] playerSettings = ctx.client.getPlayerSettings();
        return playerSettings != null ? playerSettings : new int[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerSetting(int index) {
        int[] settings = ctx.client.getPlayerSettings();
        return settings != null && index < settings.length ? settings[index] : -1;
    }
}
