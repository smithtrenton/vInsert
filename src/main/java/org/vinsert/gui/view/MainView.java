package org.vinsert.gui.view;


import org.vinsert.Application;
import org.vinsert.gui.component.tab.AbstractTab;
import org.vinsert.gui.component.tab.AddTab;
import org.vinsert.gui.component.tab.BotTab;
import org.vinsert.gui.component.tab.OverviewTab;
import org.vinsert.gui.controller.MainController;
import org.vinsert.gui.icons.Icons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static org.vinsert.gui.ControllerManager.get;

/**
 * @author const_
 * @author A_C
 */
public final class MainView extends JFrame {
    private final DebugCheckBox mouse = new DebugCheckBox("Mouse", "mouse");
    private final DebugCheckBox[] chkbxDebugs = {
            new DebugCheckBox("Camera", "camera"),
            new DebugCheckBox("Login", "login"),
            new DebugCheckBox("Npcs", "npcs"),
            new DebugCheckBox("Players", "players"),
            new DebugCheckBox("Inventory", "inventory"),
            new DebugCheckBox("Loot", "loot"),
            new DebugCheckBox("Menu", "menu"),
            mouse,
            new DebugCheckBox("Position", "position"),
            new DebugCheckBox("Floor", "floor"),
            new DebugCheckBox("Boundaries", "boundaries"),
            new DebugCheckBox("Interactable", "interactable_objects"),
            new DebugCheckBox("Wall objects", "wall_objects"),
            new DebugCheckBox("Widget Explorer", "widgetExplorer"),
            new DebugCheckBox("Settings Explorer", "settingsExplorer"),
            new DebugCheckBox("Equipment", "equipment"),
            new DebugCheckBox("Console", "console")
    };
    private final JButton btnStopScript;
    private final JButton btnRunScript;

    private JPanel displayPanel;

    private final JButton btnInput;
    private JButton btnRenderer;
    private JToolBar toolBar;
    private int lastAddedIdx = 1;
    private List<AbstractTab> tabs = new ArrayList<>();
    private JSeparator toolBarSep;
    private int sepIndex = 0;

    public MainView(final MainController mainController) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("vInsert v" + Application.VERSION + " The Free Open Source OldSchool Bot");
        setSize(770, 590);
        mouse.setSelected(true);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmNewSession = new JMenuItem("New session");
        mntmNewSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.openSession(false);
            }
        });
        mnFile.add(mntmNewSession);

        JMenuItem mntmLoadExistingSession = new JMenuItem("Load existing session");
        mntmLoadExistingSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.openSession(true);
            }
        });
        mnFile.add(mntmLoadExistingSession);

        JMenuItem mntmCloseSession = new JMenuItem("Close session");
        mntmCloseSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.closeSession();
            }
        });
        mnFile.add(mntmCloseSession);

        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);

        JMenuItem mntmAccounts = new JMenuItem("Accounts");
        mntmAccounts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.showAccounts();
            }
        });
        mnEdit.add(mntmAccounts);

        JMenuItem breaks = new JMenuItem("Breaks");
        breaks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.showBreaks();
            }
        });
        mnEdit.add(breaks);
        JMenu mnDebug = new JMenu("Debug");
        menuBar.add(mnDebug);
        for (DebugCheckBox cbx : chkbxDebugs) {
            cbx.setActionCommand("Debug:" + cbx.getInternal());
            cbx.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainController.toggleDebug(e);
                }
            });
            mnDebug.add(cbx);
        }

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        OverviewTab overviewTab = new OverviewTab();
        toolBar.add(overviewTab);
        AddTab addTab = new AddTab();
        toolBar.add(addTab);
        tabs.add(overviewTab);
        overviewTab.setEnabled(false);
        toolBarSep = new JSeparator();
        toolBarSep.setMaximumSize(new Dimension(480, 10));
        toolBarSep.setPreferredSize(new Dimension(480, 10));
        toolBarSep.setMinimumSize(new Dimension(480, 10));
        toolBar.add(toolBarSep);
        sepIndex = toolBar.getComponentIndex(toolBarSep);

        displayPanel = new JPanel();
        displayPanel.setSize(775, 530);
        displayPanel.add(overviewTab.getPanel());
        getContentPane().add(displayPanel, BorderLayout.CENTER);
        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        toolBar.add(separator);
        separator.setMaximumSize(new Dimension(10, 30));

        btnRunScript = new JButton();
        btnRunScript.setIcon(Icons.PLAY);
        btnRunScript.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.startScript();
                mainController.refresh();
            }
        });
        toolBar.add(btnRunScript);
        toolBar.addSeparator(new Dimension(5, 10));

        btnStopScript = new JButton("");
        btnStopScript.setIcon(Icons.STOP);
        btnStopScript.setEnabled(false);
        btnStopScript.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.stopScript();
                mainController.refresh();
            }
        });
        toolBar.add(btnStopScript);
        toolBar.addSeparator(new Dimension(5, 10));

        btnInput = new JButton("Input");
        btnInput.setIcon(Icons.ENABLED);
        btnInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.toggleInput();
            }
        });
        toolBar.add(btnInput);
        toolBar.addSeparator(new Dimension(5, 10));

        btnRenderer = new JButton("Render");
        btnRenderer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.toggleRenderer();
            }
        });
        btnRenderer.setIcon(Icons.ENABLED);
        toolBar.add(btnRenderer);
    }


    public void setSelectedTab(AbstractTab tab) {
        displayPanel.remove(0);
        displayPanel.add(tab.getPanel());
        ((MainController) get(MainController.class)).refresh();
        revalidate();
    }

    public void addTab(BotTab tab) {
        tabs.add(tab);
        toolBar.remove(lastAddedIdx);
        toolBar.add(tab, lastAddedIdx);
        lastAddedIdx++;
        toolBar.add(new AddTab(), lastAddedIdx);
        setSelectedTab(tab);
        for (AbstractTab _tab : tabs) {
            _tab.setEnabled(true);
        }
        tab.setEnabled(false);
        toolBar.remove(toolBarSep);
        toolBarSep.setMaximumSize(new Dimension(toolBarSep.getWidth() - 28, 10));
        toolBarSep.setPreferredSize(new Dimension(toolBarSep.getWidth() - 28, 10));
        toolBarSep.setMinimumSize(new Dimension(toolBarSep.getWidth() - 28, 10));
        toolBar.add(toolBarSep, sepIndex + tabs.size());
        ((MainController) get(MainController.class)).refresh();
        revalidate();
    }

    public void removeTab() {
        lastAddedIdx--;
    }

    public List<AbstractTab> getTabs() {
        return tabs;
    }

    public JButton getStartButton() {
        return btnRunScript;
    }

    public JButton getStopButton() {
        return btnStopScript;
    }

    public JButton getInputButton() {
        return btnInput;
    }

    public JButton getRendererButton() {
        return btnRenderer;
    }

    public DebugCheckBox[] getDebugs() {
        return chkbxDebugs.clone();
    }

    public JPanel getDisplayPanel() {
        return displayPanel;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public static final class DebugCheckBox extends JCheckBoxMenuItem {
        private final String internal;

        public DebugCheckBox(String name, String internal) {
            super(name);
            this.internal = internal;
        }

        public String getInternal() {
            return internal;
        }
    }
}

