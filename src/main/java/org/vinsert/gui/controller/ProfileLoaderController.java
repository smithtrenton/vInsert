package org.vinsert.gui.controller;

import org.vinsert.core.model.BreakProfile;
import org.vinsert.gui.Controller;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.view.ProfileLoaderView;

import javax.swing.*;
import java.awt.*;

/**
 * @author const_
 */
public final class ProfileLoaderController extends Controller<ProfileLoaderView> {

    private ProfileLoaderView view;
    private ProfileController profileController;

    public ProfileLoaderController() {
        ControllerManager.add(ProfileLoaderController.class, this);
    }

    @Override
    public ProfileLoaderView getComponent() {
        if (view == null) {
            view = new ProfileLoaderView(this);
        }
        return view;
    }

    @Override
    public boolean isComponentInitiated() {
        return view != null;
    }

    public void show(ProfileController profileController) {
        this.profileController = profileController;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setVisible(true);
        load();
    }

    private void load() {
        DefaultListModel<BreakProfile> model = new DefaultListModel<>();
        java.util.List<BreakProfile> profiles = profileController.getContainer().getAll();
        for (BreakProfile profile : profiles) {
            model.addElement(profile);
        }
        getComponent().getProfileList().setModel(model);
    }

    public void onDelete() {
        BreakProfile toRemove = view.getProfileList().getSelectedValue();
        if (toRemove != null) {
            profileController.getContainer().remove(toRemove);
        }
    }

    public void onLoad() {
        BreakProfile toLoad = view.getProfileList().getSelectedValue();
        if (toLoad != null) {
            profileController.load(toLoad);
            getComponent().dispose();
        }
    }
}
