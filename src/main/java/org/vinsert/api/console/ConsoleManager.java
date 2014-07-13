package org.vinsert.api.console;

import com.google.inject.Injector;
import org.vinsert.api.APIModule;
import org.vinsert.api.MethodContext;
import org.vinsert.api.console.impl.*;
import org.vinsert.core.Session;
import org.vinsert.gui.controller.ConsoleController;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.inject.Guice.createInjector;

/**
 *
 */
public final class ConsoleManager {
    private final List<ConsoleCommand> commands = newArrayList();
    private final Session session;
    private ScriptEngine engine;
    private String storedCommand = "";
    private ConsoleController controller;


    public ConsoleManager(Session session, ConsoleController controller) {
        this.session = session;
        this.controller = controller;
        loadCommands();
        init("groovy");
        String response = "Welcome to the developer console.\n";
        response += "This console can be used by developers to test snippets and gather information.\n";
        response += "The usage of this console is not recommended for normal users.\n";
        response += "Use ctx. when using the api, e.g ctx.objects.find(1)\n";
        renderResult(response);
    }

    /**
     * Initialises the interpreter.
     */
    public void init(String engineName) {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName(engineName);

        MethodContext context = new MethodContext();
        APIModule bindings = new APIModule();
        bindings.setContext(context);
        Injector injector = createInjector(bindings);
        injector.injectMembers(context);
        context.session = session;
        context.client = session.getClient();
        engine.put("ctx", context);
    }

    /**
     * Initialises the commands
     */
    private void loadCommands() {
        commands.add(new PrintCommand());
        commands.add(new SwitchCommand(this));
        commands.add(new ExecuteCommand(this));
        commands.add(new ToggleCommand(this));
        commands.add(new HelpCommand(this));
    }

    public void handleCommand(String command) {
        if (command.startsWith("//")) { // this is a comment.

        } else if (command.endsWith("\\")) { // this is a multi-line command.
            this.storedCommand += command + "\n";
        } else if (this.storedCommand.length() > 0) {
            this.storedCommand += command;
            evalAndReport(this.storedCommand);
            this.storedCommand = "";
        } else {
            evalAndReport(command);
        }
    }

    private void evalAndReport(String command) {
        ConsoleCommand handler = getHandler(command);
        renderResult(handler.execute(command));
    }

    private void renderResult(String result) {
        if (!result.equals("null")) {
            String[] lines = result.split("\n");
            for (String line : lines) {
                if (line.length() > 0) {
                    controller.renderResult(line);
                }
            }
        }
        controller.renderHeader();
    }

    private ConsoleCommand getHandler(String command) {
        for (ConsoleCommand handler : commands) {
            if (handler.accept(command)) {
                return handler;
            }
        }
        return new JSR223Command(engine);
    }

    public void registerCommand(ConsoleCommand command) {
        commands.add(command);
    }

    public void unregisterCommand(ConsoleCommand command) {
        commands.remove(command);
    }

    public Session getSession() {
        return session;
    }

    public List<ConsoleCommand> getCommands() {
        return commands;
    }
}
