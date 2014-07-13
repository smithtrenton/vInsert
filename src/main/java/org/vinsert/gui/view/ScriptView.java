package org.vinsert.gui.view;


import com.google.inject.Inject;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.model.Account;
import org.vinsert.core.script.stub.AgnosticStub;
import org.vinsert.gui.component.LoadingBar;
import org.vinsert.gui.controller.ScriptController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author const_
 */
public final class ScriptView extends JDialog implements ActionListener {
    private final ScriptController controller;
    private final JComboBox<Account> accounts;
    private final JTable table;
    private final LoadingBar bar;
    private final JButton btnOk;
    private final JCheckBox breaksChkBox;

    private List<AgnosticStub<AbstractScript, ScriptManifest>> scripts = new ArrayList<>();

    @Inject
    public ScriptView(ScriptController controller) {
        this.controller = controller;

        setTitle("Script Selector");
        setSize(new Dimension(680, 520));
        getContentPane().setLayout(null);

        btnOk = new JButton("OK");
        btnOk.setActionCommand("ok");
        btnOk.addActionListener(this);
        btnOk.setBounds(461, 446, 60, 23);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setActionCommand("cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(371, 446, 80, 23);
        getContentPane().add(btnCancel);

        breaksChkBox = new JCheckBox("Use Breaks?");
        breaksChkBox.setBounds(106, 446, 95, 23);
        getContentPane().add(breaksChkBox);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(6, 6, 635, 428);
        getContentPane().add(scrollPane);

        table = new JTable();
        table.setFillsViewportHeight(true);
        scrollPane.setViewportView(table);
        reinitializeTable();
        accounts = new JComboBox<Account>();
        accounts.addItem(null);
        for (Account account : Account.getAll()) {
            accounts.addItem(account);
        }
        accounts.setBounds(211, 446, 150, 23);
        getContentPane().add(accounts);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(6, 446, 90, 23);
        btnRefresh.setActionCommand("refresh");
        btnRefresh.addActionListener(this);
        getContentPane().add(btnRefresh);

        bar = new LoadingBar();
        bar.setBounds(531, 446, 110, 23);
        getContentPane().add(bar);
    }

    public void reinitializeTable() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Object[] columns = new Object[]{"Name", "Description", "Category", "Version", "Author", "Link"};
                Object[][] dataSet = new Object[scripts.size()][columns.length];
                int dataSetRowIndex = 0;
                for (AgnosticStub<AbstractScript, ScriptManifest> stub : scripts) {
                    Object[] data = new Object[columns.length];
                    data[0] = stub.manifest().name();
                    data[1] = stub.manifest().description();
                    data[2] = stub.manifest().category();
                    data[3] = stub.manifest().version();
                    data[4] = stub.manifest().author();
                    data[5] = stub.manifest().forumLink();

                    dataSet[dataSetRowIndex] = data;
                    dataSetRowIndex++;
                }
                table.setModel(new DefaultTableModel(dataSet, columns));
                table.repaint();

            }
        });
    }

    public void setAvailableScripts(List<AgnosticStub<AbstractScript, ScriptManifest>> scripts) {
        this.scripts = scripts;
        reinitializeTable();
    }

    public Account getSelectedAccount() {
        return (Account) accounts.getSelectedItem();
    }

    public void setAvailableAccounts(List<Account> accounts) {
        this.accounts.setModel(new DefaultComboBoxModel<Account>(
                accounts.toArray(new Account[accounts.size()])));
    }

    public AgnosticStub<AbstractScript, ScriptManifest> getSelectedScript() {
        int selectedIdx = table.getSelectedRow();
        if (selectedIdx == -1) {
            return null;
        }
        return scripts.size() > selectedIdx ? scripts.get(selectedIdx) : null;
    }

    public LoadingBar getBar() {
        return bar;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getBtnOk() {
        return btnOk;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "ok":
                controller.onOk();
                break;

            case "cancel":
                controller.onCancel();
                break;

            case "refresh":
                controller.onRefresh();
                break;
        }
    }

    public JCheckBox getBreaksChkBox() {
        return breaksChkBox;
    }
}
