package org.vinsert.api.console.impl;

import org.vinsert.api.console.ConsoleCommand;
import org.vinsert.api.console.ConsoleManager;

/**
 *
 */
public final class SwitchCommand implements ConsoleCommand {
    private final ConsoleManager manager;

    public SwitchCommand(ConsoleManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean accept(String text) {
        return text.startsWith("switch");
    }

    @Override
    public String execute(String text) {
        switch (text.toLowerCase().substring(7)) {
            case "groovy":
                manager.init("groovy");
                return "Switched to groovy.";

            case "clojure":
                manager.init("Clojure");
                return "Switched to clojure.";

            default:
                return "Unknown scripting lang '" + text + "'";
        }
    }

    @Override
    public String getUsage() {
        return "switch <clojure|groovy> - Switches the language of the console scripting engine.";
    }
}
