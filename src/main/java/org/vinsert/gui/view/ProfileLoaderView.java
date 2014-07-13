package org.vinsert.gui.view;

import org.vinsert.core.model.BreakProfile;
import org.vinsert.gui.controller.ProfileLoaderController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author const_
 */
public final class ProfileLoaderView extends JFrame implements ActionListener {

    private JList<BreakProfile> profileList;
    private ProfileLoaderController profileLoaderController;

    public ProfileLoaderView(ProfileLoaderController profileLoaderController) {
        this.profileLoaderController = profileLoaderController;
        setTitle("Profile Loader - vInsert");
        setBounds(100, 100, 369, 300);
        getContentPane().setLayout(null);

        profileList = new JList<>();
        profileList.setModel(new DefaultListModel<BreakProfile>());
        profileList.setBounds(12, 47, 245, 214);
        getContentPane().add(profileList);

        JLabel lblProfileNames = new JLabel("Profile Names:");
        lblProfileNames.setBounds(12, 12, 112, 15);
        getContentPane().add(lblProfileNames);

        JButton btnLoad = new JButton("Load");
        btnLoad.setBounds(269, 57, 82, 25);
        getContentPane().add(btnLoad);
        btnLoad.setActionCommand("load");
        btnLoad.addActionListener(this);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(269, 94, 82, 25);
        getContentPane().add(btnDelete);
        btnDelete.setActionCommand("delete");
        btnDelete.addActionListener(this);
    }

    public JList<BreakProfile> getProfileList() {
        return profileList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "load":
                profileLoaderController.onLoad();
                break;
            case "delete":
                profileLoaderController.onDelete();
                break;
        }
    }
}
