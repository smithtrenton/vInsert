package org.vinsert.api.console.impl;

import org.vinsert.api.console.ConsoleCommand;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 *
 */
public final class JSR223Command implements ConsoleCommand {
    private final ScriptEngine engine;

    public JSR223Command(ScriptEngine engine) {
        this.engine = engine;
    }

    @Override
    public boolean accept(String text) {
        return true;
    }

    @Override
    public String execute(String text) {
        try {
            return String.valueOf(engine.eval(text));
        } catch (ScriptException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public String getUsage() {
        return "<text> - Executes the text as a command under the currently selected engine.";
    }
}
