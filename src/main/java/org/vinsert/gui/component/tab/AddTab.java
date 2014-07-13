package org.vinsert.gui.component.tab;

import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.controller.MainController;
import org.vinsert.gui.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author : const_
 */
public final class AddTab extends JButton implements ActionListener {

    public AddTab() {
        setIcon(Icons.PLUS);
        addActionListener(this);
        setToolTipText("Add");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ((MainController) ControllerManager.get(MainController.class)).openSession(false);
    }
}
