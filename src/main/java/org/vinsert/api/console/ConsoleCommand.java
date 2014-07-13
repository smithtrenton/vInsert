package org.vinsert.api.console;

/**
 * An interface for implementing console commands.
 */
public interface ConsoleCommand {

    boolean accept(String text);

    String execute(String text);

    String getUsage();

}
