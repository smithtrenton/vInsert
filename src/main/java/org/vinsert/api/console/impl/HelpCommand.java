package org.vinsert.api.console.impl;

import org.vinsert.api.console.ConsoleCommand;
import org.vinsert.api.console.ConsoleManager;

/**
 *
 */
public final class HelpCommand implements ConsoleCommand {
    private final ConsoleManager commandManager;

    public HelpCommand(ConsoleManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public boolean accept(String text) {
        return text.startsWith("help");
    }

    @Override
    public String execute(String text) {
        StringBuilder response = new StringBuilder("Available commands:\n");
        for (ConsoleCommand command : commandManager.getCommands()) {
            response.append(command.getUsage()).append("\n");
        }
        response.append("Use ctx. when using the api, e.g ctx.objects.find(1)\n");
        return response.toString();
    }

    @Override
    public String getUsage() {
        return "help - Displays information on how to use the console.";
    }
}
