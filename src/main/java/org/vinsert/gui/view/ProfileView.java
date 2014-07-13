package org.vinsert.gui.view;

import org.vinsert.core.model.BreakCondition;
import org.vinsert.gui.controller.ProfileController;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author const_
 */
public final class ProfileView extends JDialog implements ActionListener {

    private final JTable profileTable;
    private final ProfileController controller;

    public ProfileView(ProfileController controller) {
        this.controller = controller;
        setTitle("Profile Manager - vInsert");
        setBounds(100, 100, 600, 310);
        getContentPane().setLayout(null);

        profileTable = new JTable();
        profileTable.setBorder(new LineBorder(new Color(0, 0, 0)));
        profileTable.setBounds(5, 35, 433, 210);
        profileTable.setModel(new DefaultTableModel(0, 1) {
            private final Class[] columnTypes = new Class[]{
                    BreakCondition.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        getContentPane().add(profileTable);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(465, 35, 117, 25);
        getContentPane().add(btnAdd);
        btnAdd.setActionCommand("add");
        btnAdd.addActionListener(this);

        JButton btnLoad = new JButton("Load");
        btnLoad.setBounds(465, 110, 117, 25);
        getContentPane().add(btnLoad);
        btnLoad.setActionCommand("load");
        btnLoad.addActionListener(this);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(465, 147, 117, 25);
        getContentPane().add(btnSave);
        btnSave.setActionCommand("save");
        btnSave.addActionListener(this);

        JButton btnOk = new JButton("Ok");
        btnOk.setBounds(465, 184, 117, 25);
        getContentPane().add(btnOk);
        btnOk.setActionCommand("ok");
        btnOk.addActionListener(this);

        JButton btnDont = new JButton("No Breaks");
        btnDont.setBounds(465, 224, 117, 25);
        getContentPane().add(btnDont);
        btnDont.setActionCommand("dont");
        btnDont.addActionListener(this);

        JButton btnRemove = new JButton("Remove");
        btnRemove.setBounds(465, 73, 117, 25);
        getContentPane().add(btnRemove);
        btnRemove.setActionCommand("remove");
        btnRemove.addActionListener(this);

        JLabel lblType = new JLabel("Breaks");
        lblType.setBounds(5, 12, 70, 15);
        getContentPane().add(lblType);
    }

    public JTable getProfileTable() {
        return profileTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "add":
                controller.onAdd();
                break;
            case "remove":
                controller.onRemove();
                break;
            case "save":
                controller.onSave();
                break;
            case "load":
                controller.onLoad();
                break;
            case "ok":
                controller.onOk(true);
                break;
            case "dont":
                controller.onOk(false);
                break;
        }
    }
}
