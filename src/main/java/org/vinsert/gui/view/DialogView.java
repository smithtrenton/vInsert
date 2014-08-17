package org.vinsert.gui.view;

import org.vinsert.gui.icons.Icons;

import javax.swing.*;
import java.awt.*;

/**
 * @author InvokeStatic
 */
public class DialogView extends JFrame {

    public JPanel generateHeader(String title) {
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout(20, 10));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        header.setBackground(new Color(10, 10, 10));

        JLabel lblLogo = new JLabel(Icons.LOGO30);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        header.add(lblLogo, BorderLayout.EAST);

        JLabel lblAccountManager = new JLabel(title);
        lblAccountManager.setFont(new Font("Arial", Font.PLAIN, 24));
        lblAccountManager.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        header.add(lblAccountManager, BorderLayout.WEST);
        return header;
    }

    public void generateFooter() {
        // todo: add auto-generating footer
    }

}
