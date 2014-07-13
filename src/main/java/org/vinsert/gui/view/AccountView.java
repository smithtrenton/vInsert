package org.vinsert.gui.view;

import org.vinsert.api.wrappers.Skill;
import org.vinsert.core.model.Account;
import org.vinsert.gui.controller.AccountController;
import org.vinsert.util.AES;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author const_
 * @author A_C
 */
public final class AccountView extends JFrame {
    private JList<Account> lstAccounts;
    private JTextField txtUsername;
    private JPasswordField pwdPassword;
    private JTextField txtBankPin;
    private JComboBox<Skill> cbxLampSkill;

    public AccountView(final AccountController controller) {
        setTitle("Account Manager - vInsert");
        getContentPane().setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(6, 32, 172, 180);
        getContentPane().add(scrollPane);

        lstAccounts = new JList<Account>();
        lstAccounts.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Account value = lstAccounts.getSelectedValue();
                if (value != null) {
                    txtUsername.setText(value.getUsername());
                    pwdPassword.setText(value.getPassword());
                    txtBankPin.setText(value.getBankPin());
                    cbxLampSkill.setSelectedItem(value.getLampSkill());
                }
            }
        });
        scrollPane.setViewportView(lstAccounts);

        JLabel lblAccounts = new JLabel("Accounts:");
        lblAccounts.setBounds(6, 6, 81, 16);
        getContentPane().add(lblAccounts);

        JPanel pnlAccount = new JPanel();
        pnlAccount.setBorder(new LineBorder(new Color(0, 0, 0)));
        pnlAccount.setBounds(190, 32, 404, 180);
        getContentPane().add(pnlAccount);
        pnlAccount.setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(12, 12, 90, 15);
        pnlAccount.add(lblUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(12, 39, 90, 15);
        pnlAccount.add(lblPassword);

        txtUsername = new JTextField();
        txtUsername.setBounds(120, 10, 200, 19);
        pnlAccount.add(txtUsername);
        txtUsername.setColumns(10);

        pwdPassword = new JPasswordField();
        pwdPassword.setText("Password");
        pwdPassword.setBounds(120, 37, 200, 19);
        pnlAccount.add(pwdPassword);

        JLabel lblBankPin = new JLabel("Bank PIN:");
        lblBankPin.setBounds(12, 66, 60, 15);
        pnlAccount.add(lblBankPin);

        txtBankPin = new JTextField();
        txtBankPin.setText("Bank PIN");
        txtBankPin.setBounds(120, 64, 200, 19);
        pnlAccount.add(txtBankPin);
        txtBankPin.setColumns(10);

        JLabel lblLampSkill = new JLabel("Lamp skill:");
        lblLampSkill.setBounds(12, 93, 90, 15);
        pnlAccount.add(lblLampSkill);

        cbxLampSkill = new JComboBox<Skill>();
        cbxLampSkill.setBounds(120, 88, 200, 24);
        for (Skill skill : Skill.values()) {
            cbxLampSkill.addItem(skill);
        }
        pnlAccount.add(cbxLampSkill);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selected = lstAccounts.getSelectedValue();
                if (selected != null) {
                    selected.setUsername(txtUsername.getText());
                    selected.setPassword(AES.encrypt(new String(pwdPassword.getPassword()),
                            AES.getMasterPassword()));
                    selected.setBankPin(txtBankPin.getText());
                    selected.setLampSkill((Skill) cbxLampSkill.getSelectedItem());
                    selected.save();
                    initList();
                }
            }
        });
        btnSave.setBounds(12, 143, 90, 25);
        pnlAccount.add(btnSave);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selected = lstAccounts.getSelectedValue();
                if (selected != null) {
                    Account.getAll().remove(selected);
                    initList();
                    lstAccounts.setSelectedIndex(getFilteredList().size() - 1);
                }
            }
        });
        btnDelete.setBounds(108, 143, 90, 25);
        pnlAccount.add(btnDelete);

        JLabel lblAccountInfo = new JLabel("Account info:");
        lblAccountInfo.setBounds(190, 7, 110, 15);
        getContentPane().add(lblAccountInfo);

        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.onOk();
                dispose();
            }
        });
        btnOk.setBounds(513, 222, 81, 25);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.onCancel();
            }
        });
        btnCancel.setBounds(395, 222, 106, 25);
        getContentPane().add(btnCancel);

        JButton lblAddNewAccount = new JButton("Add new account");
        lblAddNewAccount.setForeground(new Color(30, 144, 255));
        lblAddNewAccount.setBounds(6, 215, 172, 15);
        lblAddNewAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Account account = new Account("Username", "Password", "0000");
                account.save();
                initList();
                lstAccounts.setSelectedIndex(getFilteredList().size() - 1);
            }
        });
        getContentPane().add(lblAddNewAccount);
        setSize(610, 290);
    }

    private void initList() {
        lstAccounts.setListData(Account.getAll().toArray(new Account[Account.getAll().size()]));
        lstAccounts.repaint();
    }

    private List<Account> getFilteredList() {
        List<Account> filteredAccounts = newArrayList();
        for (Account account : Account.getAll()) {
            if (account.isPurgeMarker()) {
                continue;
            }
            filteredAccounts.add(account);
        }
        return filteredAccounts;
    }

    public void setAccounts(List<Account> accounts) {
        lstAccounts.setListData(accounts.toArray(new Account[accounts.size()]));
        lstAccounts.repaint();
    }

    public Account[] getAccounts() {
        Account[] accounts = new Account[lstAccounts.getModel().getSize()];
        for (int i = 0; i < lstAccounts.getModel().getSize(); i++) {
            accounts[i] = lstAccounts.getModel().getElementAt(i);
        }
        return accounts;
    }
}
