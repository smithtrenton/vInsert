package org.vinsert.gui.view;

import org.vinsert.gui.component.Console;
import org.vinsert.gui.controller.ConsoleController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author : const_
 */
public final class ConsoleView extends JDialog implements ActionListener {

    private Console console;
    private ConsoleController controller;

    public ConsoleView(ConsoleController controller) {
        this.controller = controller;
        setSize(600, 300);
        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);
        JMenu file = new JMenu("File");
        JMenuItem clear = new JMenuItem("Clear");
        clear.setActionCommand("clear");
        clear.addActionListener(this);
        file.add(clear);
        JMenuItem close = new JMenuItem("Close");
        close.setActionCommand("close");
        close.addActionListener(this);
        file.add(clear);
        menu.add(file);
        console = new Console(this);
        console.setLayout(new GridLayout(1, 1));
        console.setHeader(controller.getSession().getEnvironment().getName());
        JScrollPane pane = new JScrollPane(console, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        getContentPane().add(pane);
        setVisible(true);
    }

    public ConsoleController getController() {
        return controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "clear":
                console.clear();
                break;
            case "close":
                dispose();
                break;
        }
    }

    public Console getConsole() {
        return console;
    }
}
