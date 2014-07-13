package org.vinsert.core.control;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.debug.impl.*;
import org.vinsert.core.Session;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * A control class for managing debugging instances.
 */
public final class DebugManager {
    private final List<AbstractDebug> debugs = newArrayList();
    private final Session session;

    public DebugManager(Session session) {
        this.session = session;
        this.initDefaults();
    }

    private void initDefaults() {
        debugs.add(new MouseDebug(session));
        debugs.add(new CameraDebug(session));
        debugs.add(new PositionDebug(session));
        debugs.add(new InventoryDebug(session));
        debugs.add(new LoginDebug(session));
        debugs.add(new PlayerDebug(session));
        debugs.add(new NpcDebug(session));
        debugs.add(new WallObjectDebug(session));
        debugs.add(new FloorObjectDebug(session));
        debugs.add(new InteractableObjectDebug(session));
        debugs.add(new BoundaryObjectDebug(session));
        debugs.add(new LootDebug(session));
        debugs.add(new MenuDebug(session));
        debugs.add(new WidgetDebug(session));
    }

    public boolean isEnabled(String code) {
        for (AbstractDebug debug : debugs) {
            if (debug.isEnabled() && debug.getShortcode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public void toggle(String code, boolean enable) {
        for (AbstractDebug debug : debugs) {
            if (debug.getShortcode().equals(code)) {
                debug.setEnabled(enable);
                break;
            }
        }
    }

    public List<AbstractDebug> getDebugs() {
        return debugs;
    }

}
