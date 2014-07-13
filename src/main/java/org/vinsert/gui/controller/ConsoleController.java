package org.vinsert.gui.controller;

import org.vinsert.api.console.ConsoleManager;
import org.vinsert.core.Session;
import org.vinsert.gui.Controller;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.view.ConsoleView;

import java.awt.*;

/**
 * @author : const_
 */
public final class ConsoleController extends Controller<ConsoleView> {

    private ConsoleView view;
    private Session session;
    private ConsoleManager manager;

    public ConsoleController() {
        ControllerManager.add(ConsoleController.class, this);
    }

    @Override
    public boolean isComponentInitiated() {
        return view != null;
    }

    @Override
    public ConsoleView getComponent() {
        if (view == null) {
            view = new ConsoleView(this);
        }
        return view;
    }

    public void renderHeader() {
        getComponent().getConsole().setText(getComponent().getConsole().getText() + "\n" +
                getComponent().getConsole().getHeader());
    }

    public void renderResult(String result) {
        getComponent().getConsole().setText(getComponent().getConsole().getText() + "\n" + result);
    }

    public void handleCommand(final String cmd) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                manager.handleCommand(cmd);
            }
        };
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        t.start();
    }

    public void show(Session session) {
        this.session = session;
        getComponent().setTitle("Console @ [" + session.getEnvironment().getName() + "]");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setVisible(true);
        if (manager == null) {
            manager = new ConsoleManager(session, this);
        }
    }

    public void setName(String name) {
        getComponent().setTitle("Console @ [" + name + "]");
        getComponent().getConsole().setHeader(name);
    }

    public Session getSession() {
        return session;
    }
}
