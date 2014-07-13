package org.vinsert.gui.controller;

import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Skill;
import org.vinsert.core.model.BreakConditionType;
import org.vinsert.gui.Controller;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.view.ConditionCreatorView;
import org.vinsert.gui.view.ProfileView;

import javax.swing.*;
import java.awt.*;

/**
 * @author const_
 */
public final class ConditionCreatorController extends Controller<ConditionCreatorView> {

    private ConditionCreatorView view;
    private ProfileController profileController;

    public ConditionCreatorController() {
        ControllerManager.add(ConditionCreatorController.class, this);
    }

    @Override
    public ConditionCreatorView getComponent() {
        if (view == null) {
            view = new ConditionCreatorView(this);
        }
        return view;
    }

    @Override
    public boolean isComponentInitiated() {
        return view != null;
    }

    public ProfileView getProfileView() {
        return profileController.getComponent();
    }

    public void show(ProfileController profileController) {
        this.profileController = profileController;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setVisible(true);
    }

    public void onTypeChange() {
        switch (getComponent().getTypeComboBox().getItemAt(getComponent().getTypeComboBox().getSelectedIndex())) {
            case RUNTIME:
                getComponent().prepareMinutesTime();
                break;
            case TIME:
                getComponent().prepareHourTime();
                break;
            case SKILL:
                getComponent().prepareSkills();
                break;
        }
    }

    public void onAdd() {
        BreakConditionType type = getComponent().getTypeComboBox().getItemAt(getComponent().getTypeComboBox().getSelectedIndex());
        int min = -1;
        int max = -1;
        String text = getComponent().getSleepField().getText() + getComponent().getSleep2Field().getText() + getComponent().getTimeField().getText() +
                getComponent().getTime2Field().getText();
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                continue;
            }
            JOptionPane.showMessageDialog(getComponent(), "You inputted text.");
            return;
        }
        if (getComponent().getSleepField().getText().length() == 0 ||
                getComponent().getSleep2Field().getText().length() == 0 &&
                        (getComponent().getTime2Field().getText().length() == 0 ||
                                getComponent().getTimeField().getText().length() == 0)) {
            JOptionPane.showMessageDialog(getComponent(), "Invalid input.");
            return;
        }
        int sleep = Utilities.random(Integer.parseInt(getComponent().getSleepField().getText()),
                Integer.parseInt(getComponent().getSleep2Field().getText()));
        Skill skill = null;
        switch (type) {
            case SKILL:
                skill = getComponent().getSkills().getItemAt(getComponent().getSkills().getSelectedIndex());
                break;
        }
        min = Integer.parseInt(getComponent().getTimeField().getText());
        max = Integer.parseInt(getComponent().getTime2Field().getText());
        profileController.addSkillRow(type, skill, min, max, sleep);
        getComponent().dispose();
    }
}
