package org.vinsert.gui.view;

import org.vinsert.api.wrappers.Skill;
import org.vinsert.core.model.BreakConditionType;
import org.vinsert.gui.controller.ConditionCreatorController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author const_
 */
public final class ConditionCreatorView extends JFrame implements ActionListener {
    private final ConditionCreatorController controller;
    private final JPanel skillsPanel;
    private final JPanel runtimePanel;
    private final JPanel timePanel;
    private final JTextField sleepTextField;
    private final JTextField sleep2TextField;
    private final JTextField betweenTextField;
    private final JTextField between2TextField;
    private final JComboBox<Skill> skillComboBox;
    private final JComboBox<BreakConditionType> typeComboBox;

    public ConditionCreatorView(ConditionCreatorController controller) {
        this.controller = controller;
        setTitle("Condition Creator - vInsert");
        getContentPane().setLayout(null);
        getContentPane().setBounds(100, 100, 500, 230);
        setSize(500, 230);
        skillsPanel = new JPanel();
        timePanel = new JPanel();
        runtimePanel = new JPanel();

        JLabel lblType = new JLabel("Type:");
        lblType.setBounds(12, 12, 70, 15);
        getContentPane().add(lblType);

        skillComboBox = new JComboBox<>(Skill.values());
        typeComboBox = new JComboBox<>(BreakConditionType.values());
        typeComboBox.setActionCommand("type");

        sleepTextField = new JTextField();
        sleepTextField.setBounds(136, 104, 80, 19);
        getContentPane().add(sleepTextField);
        sleepTextField.setColumns(10);

        sleep2TextField = new JTextField();
        sleep2TextField.setBounds(275, 104, 80, 19);
        getContentPane().add(sleep2TextField);
        sleep2TextField.setColumns(10);

        betweenTextField = new JTextField();
        betweenTextField.setBounds(136, 58, 80, 19);
        getContentPane().add(betweenTextField);
        betweenTextField.setColumns(10);

        JLabel lblAmpersand1 = new JLabel("&");
        lblAmpersand1.setBounds(234, 58, 70, 15);
        getContentPane().add(lblAmpersand1);

        between2TextField = new JTextField();
        between2TextField.setColumns(10);
        between2TextField.setBounds(275, 58, 80, 19);
        getContentPane().add(between2TextField);

        typeComboBox.setBounds(56, 7, 147, 24);
        typeComboBox.addActionListener(this);
        getContentPane().add(typeComboBox);

        JLabel lblAmpersand2 = new JLabel("&");
        lblAmpersand2.setBounds(234, 106, 15, 15);
        getContentPane().add(lblAmpersand2);

        JButton btnCreate = new JButton("Create");
        btnCreate.setBounds(12, 157, 117, 25);
        btnCreate.setActionCommand("create");
        btnCreate.addActionListener(this);
        getContentPane().add(btnCreate);

        JLabel lblSleepBetween = new JLabel("Sleep Between:");
        lblSleepBetween.setBounds(12, 106, 136, 15);
        getContentPane().add(lblSleepBetween);

        JLabel minutesLbl = new JLabel("(minutes)");
        minutesLbl.setBounds(362, 106, 70, 15);
        getContentPane().add(minutesLbl);

        constructSkillsPanel();
        constructTimePanel();
        constructRuntimePanel();

        skillsPanel.setVisible(false);
        timePanel.setVisible(false);
        runtimePanel.setVisible(false);
        getContentPane().add(skillsPanel);
        getContentPane().add(timePanel);
        getContentPane().add(runtimePanel);
        repaint();
    }

    private void constructTimePanel() {
        timePanel.setLayout(null);
        timePanel.setBounds(0, 0, 444, 194);

        JLabel lblAtLevel = new JLabel("Between:");
        lblAtLevel.setBounds(12, 58, 142, 15);
        timePanel.add(lblAtLevel);

        JLabel lblminutes = new JLabel("(24 hour time)");
        lblminutes.setBounds(362, 60, 90, 15);
        timePanel.add(lblminutes);
    }

    private void constructRuntimePanel() {
        runtimePanel.setLayout(null);
        runtimePanel.setBounds(0, 0, 444, 194);

        JLabel lblAtLevel = new JLabel("Between:");
        lblAtLevel.setBounds(12, 58, 142, 15);
        runtimePanel.add(lblAtLevel);

        JLabel lblminutes = new JLabel("(minutes)");
        lblminutes.setBounds(362, 60, 70, 15);
        runtimePanel.add(lblminutes);
    }

    private void constructSkillsPanel() {
        skillsPanel.setLayout(null);
        skillsPanel.setBounds(0, 0, 444, 194);

        JLabel lblSkill = new JLabel("Skill:");
        lblSkill.setBounds(234, 12, 70, 15);
        skillsPanel.add(lblSkill);

        skillComboBox.setBounds(275, 7, 157, 24);
        skillsPanel.add(skillComboBox);

        JLabel lblAtLevel = new JLabel("Between Level:");
        lblAtLevel.setBounds(12, 58, 106, 15);
        skillsPanel.add(lblAtLevel);
    }

    public void reset() {
        between2TextField.setText("");
        betweenTextField.setText("");
        sleepTextField.setText("");
        sleepTextField.setText("");
        sleep2TextField.setText("");
        skillComboBox.setSelectedIndex(0);
        typeComboBox.setSelectedIndex(0);
        prepareSkills();
    }

    public void prepareSkills() {
        skillsPanel.setVisible(true);
        timePanel.setVisible(false);
        runtimePanel.setVisible(false);
        repaint();
    }

    public void prepareHourTime() {
        skillsPanel.setVisible(false);
        timePanel.setVisible(true);
        runtimePanel.setVisible(false);
        repaint();
    }

    public void prepareMinutesTime() {
        skillsPanel.setVisible(false);
        timePanel.setVisible(false);
        runtimePanel.setVisible(true);
        repaint();
    }

    public JComboBox<BreakConditionType> getTypeComboBox() {
        return typeComboBox;
    }

    public JComboBox<Skill> getSkills() {
        return skillComboBox;
    }

    public JTextField getTimeField() {
        return betweenTextField;
    }

    public JTextField getTime2Field() {
        return between2TextField;
    }

    public JTextField getSleepField() {
        return sleepTextField;
    }

    public JTextField getSleep2Field() {
        return sleep2TextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "type":
                controller.onTypeChange();
                break;
            case "create":
                controller.onAdd();
                break;
        }
    }
}
