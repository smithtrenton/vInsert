package org.vinsert.gui.controller;

import org.apache.log4j.Logger;
import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.debug.impl.WidgetDebug;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.core.Session;
import org.vinsert.game.engine.IClient;
import org.vinsert.game.engine.extension.EventExtension;
import org.vinsert.gui.Controller;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.component.tab.AbstractTab;
import org.vinsert.gui.component.tab.BotTab;
import org.vinsert.gui.icons.Icons;
import org.vinsert.gui.view.AppletView;
import org.vinsert.gui.view.MainView;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.vinsert.gui.ControllerManager.get;


public final class MainController extends Controller<MainView> {
    private static final Logger logger = Logger.getLogger(MainController.class);
    private MainView view;
    private int count = 0;

    public MainController() {
        ControllerManager.add(MainController.class, this);
    }

    public boolean isComponentInitiated() {
        return view != null;
    }

    @Override
    public MainView getComponent() {
        if (view == null) {
            view = new MainView(this);
        }
        return view;
    }

    public void show() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setVisible(true);
        getComponent().setResizable(false);
    }

    /**
     * Opens or creates a session
     */
    public void openSession(boolean existing) {
        if (existing) {
            logger.error("Opening existing sessions is not yet implemented!");
            return;
        }
        count++;
        BotTab tab = new BotTab("Bot #" + count);
        getComponent().addTab(tab);
    }

    public void closeSession() {
        Session session = getSelectedSession();
        JPanel panel = (JPanel) getComponent().getDisplayPanel().getComponent(0);
        if (panel instanceof AppletView) {
            AppletView appletView = (AppletView) panel;
            java.util.List<AbstractTab> tabs = getComponent().getTabs();
            int index = -1;
            for (int i = 0; i < tabs.size(); i++) {
                AbstractTab tab = tabs.get(i);
                if (tab instanceof BotTab) {
                    BotTab botTab = (BotTab) tab;
                    AppletView altAppletView = (AppletView) botTab.getPanel();
                    if (appletView.equals(altAppletView)) {
                        ((BotTab) tab).getSession().getScriptManager().stop();
                        ((BotTab) tab).getSession().setState(Session.State.STOPPED);
                        index = i;
                        break;
                    }
                }
            }
            if (index != -1) {
                getComponent().getToolBar().remove(tabs.get(index));
                if (session != null &&
                        session.getAppletLoader() != null &&
                        session.getAppletLoader().getApplet() != null) {
                    session.getAppletLoader().getApplet().stop();
                }
                tabs.remove(index);
                getComponent().revalidate();
                getComponent().setSelectedTab(tabs.get(index - 1));
                getComponent().removeTab();
            }
        }
        refresh();
    }

    public void startScript() {
        Session session = getSelectedSession();
        if (session != null) {
            AbstractScript script = session.getScriptManager().getScript();
            if (script == null) {
                ((ScriptController) get(ScriptController.class)).show(session);
            } else {
                if (session.getState() == Session.State.PAUSED) {
                    session.setState(Session.State.ACTIVE);
                    refresh();
                } else {
                    session.setState(Session.State.PAUSED);
                    refresh();
                }
            }
        } else {
            logger.error("You need to select a session before you can start a script.");
        }
    }

    public void stopScript() {
        if (getSelectedSession() != null) {
            getSelectedSession().getScriptManager().stop();
            logger.info("Sent stop signal to script manager.");
        }
    }

    public void toggleBreaks() {
        Session session = getSelectedSession();
        if (session != null) {
            if (!session.getBreakManager().hasBreaks()) {
                ((ProfileController) get(ProfileController.class)).show(session);
            } else {
                session.getBreakManager().clearBreakConditions();
            }
        }
        refresh();
    }

    /**
     * Toggles the input
     */
    public void toggleInput() {
        IClient client = getSelectedSession().getClient();
        EventExtension ex = ((EventExtension) client.getMouse());
        ex.setAcceptingEvents(!ex.isAcceptingEvents());
        refresh();
    }

    public void toggleRenderer() {
        getSelectedSession().setRender(!getSelectedSession().canRender());
        getSelectedSession().getEnvironment().
                setRender(getSelectedSession().canRender());
        refresh();
    }

    /**
     * Refreshes (primarily) the toolbar
     */
    public void refresh() {
        if (getSelectedSession() != null && getSelectedSession().getClient() != null) {
            getComponent().getInputButton().setIcon(!((EventExtension) getSelectedSession().getClient().getMouse()).isAcceptingEvents() ?
                    Icons.DISABLED : Icons.ENABLED);
            getComponent().getRendererButton().setIcon(getSelectedSession().canRender() ?
                    Icons.ENABLED : Icons.DISABLED);
        }
        Session session = getSelectedSession();
        if (session != null) {
            //    getComponent().getBreaksButton().setIcon(
            //     session.getBreakManager().hasBreaks() ?
            //      Icons.ENABLED : Icons.DISABLED);
            updateScriptControls();
            updateDebugs();
        }
    }

    /**
     * Refreshes the state of the script controls
     */
    public void updateScriptControls() {
        Session session = getSelectedSession();
        if (session != null) {
            AbstractScript script = session.getScriptManager().getScript();
            if (script != null) {
                switch (session.getState()) {
                    case STOPPED:
                        getComponent().getStartButton().setActionCommand("start");
                        getComponent().getStartButton().setIcon(Icons.PLAY);
                        getComponent().getStartButton().setEnabled(true);

                        getComponent().getStopButton().setActionCommand("stop");
                        getComponent().getStopButton().setIcon(Icons.STOP);
                        getComponent().getStopButton().setEnabled(false);
                        break;

                    case PAUSED:
                        getComponent().getStartButton().setActionCommand("resume");
                        getComponent().getStartButton().setIcon(Icons.PLAY);
                        getComponent().getStartButton().setEnabled(true);

                        getComponent().getStopButton().setActionCommand("stop");
                        getComponent().getStopButton().setIcon(Icons.STOP);
                        getComponent().getStopButton().setEnabled(true);
                        break;

                    case ACTIVE:
                        getComponent().getStartButton().setIcon(Icons.PAUSE);
                        getComponent().getStartButton().setActionCommand("pause");
                        getComponent().getStartButton().setEnabled(true);

                        getComponent().getStopButton().setActionCommand("stop");
                        getComponent().getStopButton().setIcon(Icons.STOP);
                        getComponent().getStopButton().setEnabled(true);
                        break;
                }
            } else {
                getComponent().getStartButton().setActionCommand("start");
                getComponent().getStartButton().setIcon(Icons.PLAY);
                getComponent().getStartButton().setEnabled(true);

                getComponent().getStopButton().setActionCommand("stop");
                getComponent().getStopButton().setIcon(Icons.STOP);
                getComponent().getStopButton().setEnabled(false);
            }
        }
    }

    private void updateDebugs() {
        Session session = getSelectedSession();
        if (session != null) {
            for (MainView.DebugCheckBox checkBox : getComponent().getDebugs()) {
                checkBox.setSelected(session.getDebugManager().
                        isEnabled(checkBox.getInternal()));
            }
        }
    }

    public Session getSelectedSession() {
        Component tab = getComponent().getDisplayPanel().getComponent(0);
        if (tab instanceof AppletView) {
            AppletView selectedView = (AppletView) tab;
            if (selectedView.getApplet() != null) {
                return Session.getSession(selectedView.getApplet().hashCode());
            }
        }
        return null;
    }

    public void showBreaks() {
        ((ProfileController) get(ProfileController.class)).show(getSelectedSession());
    }

    public void showAccounts() {
        ((AccountController) get(AccountController.class)).show();
    }

    public void removeTab(final AppletView appletView) {
        SwingWorker worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                if (appletView.getApplet() != null) {
                    Applet applet = appletView.getApplet();
                    Session session = Session.getSession(applet.hashCode());
                    session.getScriptManager().stop();
                    applet.stop();
                    applet.destroy();
                    applet = null;
                }
                //getComponent().getTabbedPane().remove(appletView);
                return null;
            }

        };
        worker.execute();
    }

    public void toggleDebug(ActionEvent evt) {
        if (getSelectedSession() != null) {
            if (evt.getActionCommand().contains("console")) {
                ((ConsoleController) get(ConsoleController.class)).show(getSelectedSession());
                return;
            }
            if (evt.getActionCommand().contains("settings")) {
                ((SettingsDebugController) get(SettingsDebugController.class)).show(getSelectedSession().getClient());
                return;
            }
            if (evt.getActionCommand().contains("widget")) {
                WidgetDebug debug = null;
                for (AbstractDebug abstractDebug : getSelectedSession().getDebugManager().getDebugs()) {
                    if (abstractDebug instanceof WidgetDebug) {
                        abstractDebug.setEnabled(true);
                        debug = (WidgetDebug) abstractDebug;
                        break;
                    }
                }
                ((WidgetDebugController) get(WidgetDebugController.class)).show(getSelectedSession().getClient(), debug);
                return;
            }
            getSelectedSession().getDebugManager().toggle(
                    (evt.getActionCommand().split(":")[1]),
                    ((JCheckBoxMenuItem) evt.getSource()).isSelected());
        }
    }
}
