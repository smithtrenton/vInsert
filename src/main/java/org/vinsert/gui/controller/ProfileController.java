package org.vinsert.gui.controller;


import org.vinsert.api.wrappers.Skill;
import org.vinsert.core.Session;
import org.vinsert.core.control.BreakManager;
import org.vinsert.core.model.BreakCondition;
import org.vinsert.core.model.BreakConditionType;
import org.vinsert.core.model.BreakProfile;
import org.vinsert.gui.Controller;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.view.ConditionCreatorView;
import org.vinsert.gui.view.ProfileView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static org.vinsert.gui.ControllerManager.get;


/**
 * @author const_
 */
public final class ProfileController extends Controller<ProfileView> {
    private org.vinsert.core.model.Container<BreakProfile> container;
    private Session selectedSession;
    private ProfileView view;

    public ProfileController() {
        ControllerManager.add(ProfileController.class, this);
    }

    @Override
    public ProfileView getComponent() {
        if (view == null) {
            view = new ProfileView(this);
        }
        return view;
    }

    @Override
    public boolean isComponentInitiated() {
        return view != null;
    }

    public void show(Session selectedSession) {
        container = new org.vinsert.core.model.Container<BreakProfile>("profiles");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setVisible(true);
        this.selectedSession = selectedSession;
    }

    public void addSkillRow(BreakConditionType type, Skill skill, int min, int max, int sleep) {
        BreakCondition bre = null;
        switch (type) {
            case SKILL:
                bre = BreakCondition.createSkill(sleep, skill, min, max);
                break;
            case RUNTIME:
                bre = BreakCondition.createRuntime(sleep, min, max);
                break;
            case TIME:
                bre = BreakCondition.createTime(sleep, min, max);
                break;
        }
        ((DefaultTableModel) getComponent().getProfileTable().getModel()).addRow(new BreakCondition[]{bre});
    }

    public void onOk(final boolean useBreaks) {
        if (useBreaks) {
            BreakManager breakManager = selectedSession.getBreakManager();
            for (int i = 0; i < getComponent().getProfileTable().getRowCount(); i++) {
                breakManager.addBreakCondition((BreakCondition) getComponent().getProfileTable().getValueAt(i, 0));
            }
        }
        getComponent().dispose();
    }

    public void onAdd() {
        ((ConditionCreatorController) get(ConditionCreatorController.class))
                .show(this);
        ((ConditionCreatorView)
                get(ConditionCreatorController.class).getComponent()).reset();
    }

    public void onRemove() {
        if (getComponent().getProfileTable().getSelectedRow() != -1) {
            ((DefaultTableModel) getComponent().getProfileTable().getModel()).
                    removeRow(getComponent().getProfileTable().getSelectedRow());
        }
    }

    public void onLoad() {
        ((ProfileLoaderController) get(ProfileLoaderController.class)).show(this);
    }

    public void load(BreakProfile profile) {
        DefaultTableModel model = new DefaultTableModel(0, 1) {
            private final Class[] columnTypes = new Class[]{
                    BreakCondition.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        };
        for (String raw : profile.getConditionsString().split("!")) {
            model.addRow(new BreakCondition[]{BreakCondition.fromString(raw)});
        }
        getComponent().getProfileTable().setModel(model);
    }

    public void onSave() {
        String name = JOptionPane.showInputDialog(view, "Please enter a name to save this profile under:");
        BreakProfile profile = new BreakProfile(name);
        for (int i = 0; i < getComponent().getProfileTable().getRowCount(); i++) {
            profile.addConditions((BreakCondition) getComponent().getProfileTable().getValueAt(i, 0));
        }
        container.add(profile);
        container.save();
    }

    public org.vinsert.core.model.Container<BreakProfile> getContainer() {
        return container;
    }
}
