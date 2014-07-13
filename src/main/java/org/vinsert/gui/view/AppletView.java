package org.vinsert.gui.view;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

/**
 * The AppletView is responsible for rendering an applet (or loading message if its bot done yet).
 *
 * @author const_
 */
public final class AppletView extends JPanel {
    private JLabel lblNewLabel;
    private int tabIndex = 0;
    private Applet applet;

    public AppletView() {
        setPreferredSize(new Dimension(765, 503));
        setMaximumSize(new Dimension(765, 503));
        setLayout(null);

        lblNewLabel = new JLabel("Loading, please wait...");
        lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 36));
        lblNewLabel.setBounds(169, 169, 572, 148);
        add(lblNewLabel);
    }


    public void update() {
        if (applet != null) {
            lblNewLabel.setVisible(false);
            applet.setSize(getWidth(), getHeight());
            applet.setVisible(true);
            add(applet, BorderLayout.CENTER);
        }
    }

    public Applet getApplet() {
        return applet;
    }

    public void setApplet(final Applet applet) {
        final Applet self = this.applet;
        if (applet == null) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (self != null) {
                        remove(self);
                    }
                    lblNewLabel.setVisible(true);
                }
            });
        } else {
            this.applet = applet;
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    lblNewLabel.setVisible(false);
                    applet.setSize(getWidth(), getHeight());
                    applet.setVisible(true);
                    add(applet, BorderLayout.CENTER);
                }
            });
        }
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
}
