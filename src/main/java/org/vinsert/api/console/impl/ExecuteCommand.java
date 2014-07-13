package org.vinsert.api.console.impl;

import org.vinsert.api.console.ConsoleCommand;
import org.vinsert.api.console.ConsoleManager;

/**
 * Execute script command
 *
 * @author tommo
 */
public final class ExecuteCommand implements ConsoleCommand {

    public ExecuteCommand(ConsoleManager consoleManager) {
    }

    @Override
    public boolean accept(String text) {
        return text.startsWith("execute");
    }

    @Override
    public String execute(String text) {
        String script = text.substring("execute".length()).trim();

        /*if (script.equals("explorer")) {
            AgnosticStub<WidgetExplorerScript, ScriptManifest> stub = ClassFileScriptStub.create(WidgetExplorerScript.class, ScriptManifest.class);
            consoleManager.getSession().getScriptManager().start(stub);
            return "Executed script: " + script;
        }*/
        return "No such script: " + script;
    }

    @Override
    public String getUsage() {
        return "execute <script> - Executes a named script.";
    }

}
