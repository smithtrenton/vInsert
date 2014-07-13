package org.vinsert.gui.view;

import org.vinsert.core.model.Property;
import org.vinsert.gui.controller.UnlockController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author const_
 * @author A_C
 */
public final class UnlockView extends JDialog {
    private final JPasswordField pwdPassword;
    private JLabel lbltoProtectYour;

    public UnlockView(final UnlockController unlockController) {
        setLayout(null);
        setSize(465, 230);
        setModal(true);

        JLabel lblUnlockDatabase = new JLabel("Unlock database");
        lblUnlockDatabase.setFont(new Font("Arial", Font.PLAIN, 24));
        lblUnlockDatabase.setBounds(6, 6, 187, 50);
        add(lblUnlockDatabase);

        if (Property.get("cryptomsg") == null) {
            lbltoProtectYour = new JLabel("<html>To protect your account data and privacy we encrypt some of the information stored on your computer. Please set a password encrypt the data.");
        } else {
            lbltoProtectYour = new JLabel("<html>To protect your account data and privacy we encrypt some of the information stored on your computer. Please enter the password used to previously encrypt the data.");
        }
        lbltoProtectYour.setBounds(31, 46, 419, 50);
        add(lbltoProtectYour);

        JSeparator separator = new JSeparator();
        separator.setBounds(0, 101, 463, 18);
        add(separator);

        final JLabel lblDatabasePassword = new JLabel("Database password:");
        lblDatabasePassword.setBounds(20, 118, 133, 16);
        add(lblDatabasePassword);

        pwdPassword = new JPasswordField();
        pwdPassword.setBounds(179, 112, 265, 28);
        pwdPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    lblDatabasePassword.requestFocus();
                    unlockController.unlock();
                }
                super.keyReleased(e);
            }
        });
        add(pwdPassword);

        JButton btnUnlock = new JButton("Unlock");
        btnUnlock.setBounds(361, 152, 83, 29);
        btnUnlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unlockController.unlock();
            }
        });
        add(btnUnlock);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(266, 152, 83, 29);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unlockController.cancel();
            }
        });
        add(btnCancel);
    }

    /**
     * Gets the database password
     *
     * @return password
     */
    public String getPassword() {
        return new String(pwdPassword.getPassword());
    }


}
