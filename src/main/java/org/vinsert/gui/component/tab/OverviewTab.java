package org.vinsert.gui.component.tab;

import org.vinsert.gui.icons.Icons;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author : const_
 */
public final class OverviewTab extends AbstractTab {

    private JPanel panel;

    public OverviewTab() {
        setToolTipText("Overview");
        panel = new JPanel();
        setIcon(Icons.HOME);
        panel.setPreferredSize(new Dimension(775, 530));
        panel.setMaximumSize(new Dimension(775, 530));
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Overview");
        lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 36));
        lblNewLabel.setBounds(310, 260 / 2, 572, 148);
        panel.add(lblNewLabel);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
