package org.vinsert.gui.controller;

import org.vinsert.game.engine.IClient;
import org.vinsert.gui.Controller;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.view.SettingsDebugView;

import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Date: 05/11/13
 * Time: 09:31
 *
 * @author Matt Collinge
 */
public final class SettingsDebugController extends Controller<SettingsDebugView> {

    private SettingsDebugView view;
    private IClient client;

    public SettingsDebugController() {
        ControllerManager.add(SettingsDebugController.class, this);
    }

    public void show(IClient client) {
        this.client = client;
        getComponent().setModal(false);
        refreshList();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setVisible(true);
    }

    public void hide() {
        client = null;
        getComponent().dispose();
    }

    @Override
    public SettingsDebugView getComponent() {
        if (view == null) {
            view = new SettingsDebugView(this);
        }
        return view;
    }

    @Override
    public boolean isComponentInitiated() {
        return view != null;
    }

    private void updateTable(int index) {
        if (index >= 0 && client.getWidgetSettings().length < index) {
            int settingValue = client.getWidgetSettings()[index];
            Object[][] data = new Object[][]{
                    {"Decimal", settingValue},
                    {"Hex", Integer.toHexString(settingValue)},
                    {"Binary", Integer.toBinaryString(settingValue)}
            };
            getComponent().getSettingsInfoTable().setModel(new DefaultTableModel(data, new String[]{"Data", "Value"}));
        }
    }

    private void refreshList() {
        int selectionIndex = getComponent().getSettingsList().getSelectedIndex(); // preserve selection
        int[] settingsData = client.getWidgetSettings();
        //List<String> dataList = new ArrayList<>();
        String[] data = new String[settingsData.length];
        for (int i = 0; i < settingsData.length; i++) {
            /*if (settingsData[i] > 0) {
                dataList.add(String.format("Setting %s: %s", i, settingsData[i]));
            }*/
            data[i] = String.format("Setting %s: %s", i, settingsData[i]);
        }
        getComponent().getSettingsList().setListData(data);
        //getComponent().getSettingsList().setListData(dataList.toArray(new String[dataList.size()]));
        getComponent().getSettingsList().setSelectedIndex(selectionIndex);
    }

    public void onListSelectionChanged() {
        updateTable(getComponent().getSettingsList().getSelectedIndex());
    }

    public void addSettingChange(int index, int oldValue) {
        refreshList();
        if (getComponent().getSettingsList().getSelectedIndex() == index) {
            updateTable(index);
        }
        getComponent().getSettingsLogArea().append(String.format("Setting %s: %s -> %s\n", index, oldValue, client.getWidgetSettings()[index]));
        getComponent().getSettingsLogArea().setCaretPosition(getComponent().getSettingsLogArea().getText().length());
    }

}
