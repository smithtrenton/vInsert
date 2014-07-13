package org.vinsert.api.console.impl;

import org.vinsert.api.console.ConsoleCommand;

/**
 * A command that prints something to the console.
 */
public final class PrintCommand implements ConsoleCommand {

    @Override
    public boolean accept(String text) {
        return text.startsWith("print");
    }

    @Override
    public String execute(String text) {
        return text.substring(6, text.length());
    }

    @Override
    public String getUsage() {
        return "print <text> - Prints a message to the console";
    }

}
