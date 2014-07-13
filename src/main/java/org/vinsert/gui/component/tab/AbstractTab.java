package org.vinsert.gui.component.tab;

import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.controller.MainController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author : const_
 */
public abstract class AbstractTab extends JButton implements ActionListener {

    protected AbstractTab() {
        addActionListener(this);
    }

    protected AbstractTab(String name) {
        super(name);
        addActionListener(this);
    }

    public abstract JPanel getPanel();


    @Override
    public final void actionPerformed(ActionEvent e) {
        for (AbstractTab tab : ((MainController) ControllerManager.get(MainController.class)).getComponent().getTabs()) {
            tab.setEnabled(true);
        }
        setEnabled(false);
        ((MainController) ControllerManager.get(MainController.class)).getComponent().setSelectedTab(this);
    }
}
