package org.vinsert.gui.controller;

import org.vinsert.core.model.Property;
import org.vinsert.gui.Controller;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.view.UnlockView;
import org.vinsert.util.AES;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.lang.System.getProperty;
import static org.vinsert.gui.ControllerManager.get;
import static org.vinsert.util.AES.encrypt;

/**
 *
 */
public final class UnlockController extends Controller<UnlockView> {
    private static boolean decrypted = false;
    private UnlockView view;

    public UnlockController() {
        ControllerManager.add(UnlockController.class, this);
    }

    @Override
    public UnlockView getComponent() {
        if (view == null) {
            view = new UnlockView(this);
            view.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(255);
                    super.windowClosed(e);
                }
            });
        }
        return view;
    }

    @Override
    public boolean isComponentInitiated() {
        return view != null;
    }

    public void unlock() {
        String password = view.getPassword();
        Property cryptoMessage = Property.get("cryptomsg");
        if (cryptoMessage == null) {
            cryptoMessage = new Property();
            cryptoMessage.setKey("cryptomsg");
            cryptoMessage.setValue(encrypt(getProperty("user.home"), password));
            cryptoMessage.save();
        }

        String decrypted = AES.decrypt(cryptoMessage.getValue(), password);
        if (decrypted.equalsIgnoreCase(getProperty("user.home"))) {
            AES.setMasterPassword(password);
            getComponent().dispose();
            UnlockController.decrypted = true;
            ((MainController) get(MainController.class)).show();
        } else {
            JOptionPane.showMessageDialog(getComponent(),
                    "Master password is incorrect, please try again.");
        }
    }

    public void cancel() {
        getComponent().dispose();
        System.exit(255);
    }

    public void show() {
        getComponent().setTitle("Unlock");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setModal(true);
        getComponent().setVisible(true);
    }

    public static boolean isDecrypted() {
        return decrypted;
    }

    public static void setDecrypted(boolean decrypted) {
        UnlockController.decrypted = decrypted;
    }
}
