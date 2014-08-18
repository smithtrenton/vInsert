package org.vinsert.gui.controller;

import org.vinsert.api.wrappers.Skill;
import org.vinsert.core.model.Account;
import org.vinsert.gui.Controller;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.view.AccountView;
import org.vinsert.util.AES;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public final class AccountController extends Controller<AccountView> {
    private AccountView view;

    public AccountController() {
        ControllerManager.add(AccountController.class, this);
    }

    @Override
    public boolean isComponentInitiated() {
        return view != null;
    }

    public AccountView getComponent() {
        if (view == null) {
            view = new AccountView(this);
        }
        return view;
    }

    public void show() {
        getComponent().setTitle("Account Manager - vInsert");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        SwingWorker<Void, Void> task = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                getComponent().setAccounts(Account.getAll());
                return null;
            }
        };
        task.execute();
        getComponent().setLocation(centerX, centerY);
        getComponent().setVisible(true);
    }

    public void addNewAccount() {
        Account account = new Account("Username", "Password", "0000");
        account.save();
    }

    public void saveAccount(Account selected, String username, String password, int bankPin, Skill lampSkill) {
        selected.setUsername(username);
        selected.setPassword(AES.encrypt(new String(password),
                AES.getMasterPassword()));
        selected.setBankPin(String.valueOf(bankPin));
        selected.setLampSkill(lampSkill);
        selected.save();
    }

    public void onOk() {
        for (Account account : getComponent().getAccounts()) {
            if (account.isPurgeMarker()) {
                account.remove();
                continue;
            }

            account.save();
            getComponent().dispose();
        }
    }
}
