package org.vinsert.gui.view;

import org.vinsert.api.wrappers.Skill;
import org.vinsert.core.model.Account;
import org.vinsert.gui.controller.AccountController;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        BorderLayout lytBody = new BorderLayout();
        setLayout(lytBody);
        setResizable(false);

        // ------- HEADER -------
        add(new DialogHelper().generateHeader("Account Manager"), BorderLayout.NORTH);

        // ------- BODY -------
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout(20, 20));
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(6, 32, 172, 180);
        body.add(scrollPane, BorderLayout.WEST);
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

        JPanel pnlAccount = new JPanel();
        body.add(pnlAccount, BorderLayout.CENTER);
        GroupLayout lytAccount = new GroupLayout(pnlAccount);
        pnlAccount.setLayout(lytAccount);

        lytAccount.setAutoCreateGaps(true);
        lytAccount.setAutoCreateContainerGaps(true);

        txtUsername = new JTextField();
        pwdPassword = new JPasswordField();
        txtBankPin = new JTextField();

        txtBankPin.setColumns(4);

        JLabel lblUsername = new JLabel("Username");
        JLabel lblPassword = new JLabel("Password");
        JLabel lblBankPin = new JLabel("Bank Pin");

        GroupLayout.SequentialGroup lytAccountHorizontal = lytAccount.createSequentialGroup();

        lytAccountHorizontal.addGroup(lytAccount.createParallelGroup().
                addComponent(lblUsername).
                addComponent(lblPassword));
        lytAccountHorizontal.addGroup(lytAccount.createParallelGroup().
                addComponent(txtUsername).
                addComponent(pwdPassword));
        lytAccount.setHorizontalGroup(lytAccountHorizontal);

        GroupLayout.SequentialGroup lytAccountVertical = lytAccount.createSequentialGroup();

        lytAccountVertical.addGroup(lytAccount.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(lblUsername).
                addComponent(txtUsername));
        lytAccountVertical.addGroup(lytAccount.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(lblPassword).
                addComponent(pwdPassword));
        lytAccount.setVerticalGroup(lytAccountVertical);


        add(body, BorderLayout.CENTER);

        // ------- FOOTER -------
        JPanel footer = new JPanel();
        footer.setLayout(new BorderLayout(20, 20));
        JPanel footerButtons = new JPanel();

        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.onOk();
            }
        });

        footerButtons.add(btnOk, BorderLayout.EAST);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        footerButtons.add(btnCancel, BorderLayout.WEST);

        footer.add(footerButtons, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);

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
