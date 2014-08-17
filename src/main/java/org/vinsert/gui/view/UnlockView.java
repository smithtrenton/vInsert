package org.vinsert.gui.view;

import org.vinsert.core.model.Property;
import org.vinsert.gui.controller.UnlockController;
import org.vinsert.gui.icons.Icons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * @author const_
 * @author A_C
 */
public final class UnlockView extends JDialog {
    private final JPasswordField pwdPassword;
    private JLabel lbltoProtectYour;

    public UnlockView(final UnlockController unlockController) {
        setLayout(null);
        setSize(465, 260);
        setModal(true);

        JPanel header = new JPanel();
        header.setLayout(null);
        header.setSize(465, 50);
        header.setBackground(new Color(10, 10, 10));

        JLabel lblLogo = new JLabel(Icons.LOGO30);
        lblLogo.setBounds(335, 13, 95, 30);
        header.add(lblLogo);

        JLabel lblUnlockDatabase = new JLabel("Unlock database");
        lblUnlockDatabase.setFont(new Font("Arial", Font.PLAIN, 24));
        lblUnlockDatabase.setBounds(20, 1, 300, 50);
        header.add(lblUnlockDatabase);
        add(header);

        if (Property.get("cryptomsg") == null) {
            lblUnlockDatabase.setText("Set Database Password");
            lbltoProtectYour = new JLabel("<html><p>To protect your account data and privacy, we encrypt some of the information stored on your computer. Please set a password encrypt the data.</p><br/><p><strong>Note:</strong> Your data cannot be decrypted if you lose this password.</p>");
        } else {
            lbltoProtectYour = new JLabel("<html><p>To protect your account data and privacy, we encrypt some of the information stored on your computer. Please enter the password used to previously encrypt the data.</p>");
        }
        lbltoProtectYour.setBounds(20, 60, 415, 75);
        add(lbltoProtectYour);

        final JLabel lblDatabasePassword = new JLabel("Database password:");
        lblDatabasePassword.setBounds(20, 148, 133, 16);
        add(lblDatabasePassword);

        pwdPassword = new JPasswordField();
        pwdPassword.setBounds(180, 142, 255, 28);
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
        if (Property.get("cryptomsg") == null) {
            btnUnlock.setText("Set");
        }
        btnUnlock.setBounds(351, 182, 83, 29);
        btnUnlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unlockController.unlock();
            }
        });
        add(btnUnlock);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(256, 182, 83, 29);
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
