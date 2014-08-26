package org.vinsert.gui.view;

import org.vinsert.api.wrappers.Skill;
import org.vinsert.core.model.Account;
import org.vinsert.gui.controller.AccountController;
import org.vinsert.util.AES;

import javax.swing.*;
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
 * @author InvokeStatic
 */
public final class AccountView extends JFrame implements ActionListener {
    private JList<Account> lstAccounts;
    private JTextField txtUsername;
    private JPasswordField pwdPassword;
    private JSpinner txtBankPin;
    private JComboBox<Skill> cbxLampSkill;
    private JCheckBox usePin;


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

        JPanel pnlList = new JPanel();
        pnlList.setLayout(new BorderLayout());
        body.add(pnlList, BorderLayout.WEST);

        JScrollPane scrollPane = new JScrollPane();
        pnlList.add(scrollPane, BorderLayout.CENTER);

        JButton btnAddNewAccount = new JButton("Add new account");
        btnAddNewAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.addNewAccount();
                initList();
                lstAccounts.setSelectedIndex(getFilteredList().size() - 1);
            }
        });
        pnlList.add(btnAddNewAccount, BorderLayout.SOUTH);


        lstAccounts = new JList<Account>();
        lstAccounts.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Account value = lstAccounts.getSelectedValue();
                if (value != null) {
                    txtUsername.setText(value.getUsername());
                    pwdPassword.setText(value.getPassword());
                    txtBankPin.setValue(Integer.valueOf(value.getBankPin()));
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

        txtUsername = new JTextField("Username");
        pwdPassword = new JPasswordField();
        txtBankPin = new JSpinner(new SpinnerNumberModel(0000, 0000, 9999, 1));


        cbxLampSkill = new JComboBox<Skill>();

        for (Skill skill : Skill.values()) {
            cbxLampSkill.addItem(skill);
        }

        JLabel lblUsername = new JLabel("Username");
        JLabel lblPassword = new JLabel("Password");
        JLabel lblBankPin = new JLabel("Bank Pin");
        JLabel lblLampSkill = new JLabel("Lamp Skill");

        JPanel pnlBankPin = new JPanel();
        pnlBankPin.setLayout(new FlowLayout());
        pnlBankPin.add(txtBankPin);
        usePin = new JCheckBox("Use Pin");
        usePin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(usePin.isSelected()) {
                    enableBankPin();
                } else {
                    disableBankPin();
                }
            }
        });
        pnlBankPin.add(usePin);

        JPanel pnlNull = new JPanel();

        JPanel pnlAccFooter = new JPanel();
        pnlAccFooter.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selected = lstAccounts.getSelectedValue();
                if (selected != null) {
                    controller.saveAccount(
                            selected,
                            txtUsername.getText(),
                            new String(pwdPassword.getPassword()),
                            (Integer) (txtBankPin.getValue()),
                            (Skill) cbxLampSkill.getSelectedItem()
                    );
                    initList();
                    lstAccounts.setSelectedIndex(getFilteredList().indexOf(selected));
                } else {
                    controller.addNewAccount();
                    initList();
                    lstAccounts.setSelectedIndex(getFilteredList().size() - 1);
                }
            }
        });
        btnSave.setMargin(new Insets(0, 0, 0, 5));

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

        pnlAccFooter.add(btnSave);
        pnlAccFooter.add(btnDelete);

        GroupLayout.SequentialGroup lytAccountHorizontal = lytAccount.createSequentialGroup();

        lytAccountHorizontal.addGroup(lytAccount.createParallelGroup().
                addComponent(lblUsername).
                addComponent(lblPassword).
                addComponent(lblBankPin).
                addComponent(lblLampSkill).
                addComponent(pnlNull));
        lytAccountHorizontal.addGroup(lytAccount.createParallelGroup().
                addComponent(txtUsername).
                addComponent(pwdPassword).
                addComponent(pnlBankPin).
                addComponent(cbxLampSkill).
                addComponent(pnlAccFooter));
        lytAccount.setHorizontalGroup(lytAccountHorizontal);

        GroupLayout.SequentialGroup lytAccountVertical = lytAccount.createSequentialGroup();

        lytAccountVertical.addGroup(lytAccount.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(lblUsername).
                addComponent(txtUsername));
        lytAccountVertical.addGroup(lytAccount.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(lblPassword).
                addComponent(pwdPassword));
        lytAccountVertical.addGroup(lytAccount.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(lblBankPin).
                addComponent(pnlBankPin));
        lytAccountVertical.addGroup(lytAccount.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(lblLampSkill).
                addComponent(cbxLampSkill));
        lytAccountVertical.addGroup(lytAccount.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(pnlNull).
                addComponent(pnlAccFooter));
        lytAccount.setVerticalGroup(lytAccountVertical);


        add(body, BorderLayout.CENTER);

        // ------- FOOTER -------
        JPanel footer = new JPanel();
        footer.setLayout(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        JPanel footerButtons = new JPanel();

        footer.add(new JSeparator(), BorderLayout.NORTH);

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

        pack();
    }

    public void initList() {
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

    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword() {
        return new String(pwdPassword.getPassword());
    }

    public String getBankPin() {
        return String.valueOf(txtBankPin.getValue());
    }

    public Skill getLampSkill() {
        return (Skill) cbxLampSkill.getSelectedItem();
    }

    public boolean isUsingPin() {
        return usePin.isSelected();
    }

    public void enableBankPin() {
        this.txtBankPin.setEnabled(true);
        this.txtBankPin.setValue(0);
        repaint();
    }

    public void disableBankPin() {
        this.txtBankPin.setEnabled(false);
        this.txtBankPin.setValue(0);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispatchEvent(e);
    }
}
