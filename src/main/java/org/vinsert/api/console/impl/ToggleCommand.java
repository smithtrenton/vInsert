package org.vinsert.api.console.impl;

import org.vinsert.api.console.ConsoleCommand;
import org.vinsert.api.console.ConsoleManager;
import org.vinsert.api.debug.AbstractDebug;

/**
 *
 */
public final class ToggleCommand implements ConsoleCommand {
    private final ConsoleManager commandManager;

    public ToggleCommand(ConsoleManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public boolean accept(String text) {
        return text.startsWith("toggle");
    }

    @Override
    public String execute(String text) {
        if (text.trim().equals("toggle")) {
            return getUsage();
        } else if (text.trim().equals("toggle help")) {
            String response = "Available debugs: \n";
            for (AbstractDebug debug : commandManager.getSession().getDebugManager().getDebugs()) {
                response += debug.getShortcode() + " | Enabled: " + debug.isEnabled() + "\n";
            }
            return response;
        } else {
            String toShow = text.trim().substring(7);
            for (AbstractDebug debug : commandManager.getSession().getDebugManager().getDebugs()) {
                if (debug.getShortcode().equalsIgnoreCase(toShow)) {
                    debug.setEnabled(!debug.isEnabled());
                    return "Toggled " + debug.getName() + " (Enabled: " + debug.isEnabled() + ")";
                }
            }
            return "Debug '" + toShow + "' not found, type 'toggle help' for a list of debugs.";
        }
    }

    @Override
    public String getUsage() {
        return "toggle <help|debug_name> - Toggles a part of the debugging system.";
    }
}
