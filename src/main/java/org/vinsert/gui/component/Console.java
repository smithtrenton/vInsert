package org.vinsert.gui.component;

import org.vinsert.gui.view.ConsoleView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_ENTER;

/**
 * @author : const_
 */
public final class Console extends JTextPane {

    private String header;
    private String command = "";
    private ConsoleView view;
    //private List<String> commands = new ArrayList<>(30);
    //private int lastIdx = -1;

    public Console(ConsoleView view) {
        addKeyListener(new Adapter());
        setEditable(false);
        this.view = view;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String name) {
        header = "[" + name + "]#: ";
    }

    public void clear() {
        setText(header);
        command = "";
    }

    public final class Adapter extends KeyAdapter {

        @Override
        public void keyTyped(KeyEvent e) {
            switch (e.getKeyChar()) {
             /*   case VK_UP:
                case VK_KP_UP:
                    System.out.println("UP");
                    if (lastIdx == -1) {
                        return;
                    }
                    setText(getText().substring(0, getText().length() - command.length()));
                    command = commands.get(lastIdx);
                    setText(getText() + command);
                    lastIdx--;
                    return;
                case VK_KP_DOWN:
                case VK_DOWN:
                    if (lastIdx == commands.size()) {
                        return;
                    }
                    setText(getText().substring(0, getText().length() - command.length()));
                    command = commands.get(lastIdx);
                    setText(getText() + command);
                    lastIdx++;
                    return;*/
                case VK_BACK_SPACE:
                    if (command.length() > 0) {
                        command = command.substring(0, command.length() - 1);
                        String text = getText().substring(0, getText().length() - 1);
                        setText(text);
                    }
                    return;
                case VK_ENTER:
                    view.getController().handleCommand(command);
                    // commands.add(command);
                    //   lastIdx++;
                    command = "";
                    return;
            }
            command += e.getKeyChar();
            setText(getText() + e.getKeyChar());
        }
    }

}
