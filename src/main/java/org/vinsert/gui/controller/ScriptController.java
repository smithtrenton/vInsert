package org.vinsert.gui.controller;

import org.apache.log4j.Logger;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.api.script.meta.ScriptManifest;
import org.vinsert.core.Session;
import org.vinsert.core.script.CollectiveScriptLoader;
import org.vinsert.core.script.ScriptLoaderCallback;
import org.vinsert.core.script.stub.AgnosticStub;
import org.vinsert.gui.Controller;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.view.ScriptView;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;


/**
 *
 */
public final class ScriptController extends Controller<ScriptView> {
    private final Logger logger = Logger.getLogger(ScriptController.class);
    private Session selectedSession;
    private ScriptView view;

    public ScriptController() {
        ControllerManager.add(ScriptController.class, this);
    }

    @Override
    public ScriptView getComponent() {
        if (view == null) {
            view = new ScriptView(this);
        }
        return view;
    }

    @Override
    public boolean isComponentInitiated() {
        return view != null;
    }

    public void show(Session selectedSession) {
        this.selectedSession = selectedSession;
        getComponent().setModal(true);
        getComponent().setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setVisible(true);
        onRefresh();
    }

    public void onOk() {
        final AgnosticStub<AbstractScript, ScriptManifest> script = getComponent().getSelectedScript();
        if (script != null) {
            SwingWorker worker = new SwingWorker<Void, ScriptManifest>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        if (getComponent().getSelectedAccount() != null) {
                            selectedSession.getEnvironment().setAccount(getComponent().getSelectedAccount());
                        }
                        selectedSession.getEnvironment().setBreakingEnabled(getComponent().getBreaksChkBox().isSelected());
                        selectedSession.getScriptManager().start(script);
                        logger.info("Sent start signal for " + script.manifest().name() + " by " + script.manifest().author());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            worker.execute();
            getComponent().dispose();
            return;
        }
        JOptionPane.showMessageDialog(getComponent(), "Please select a script");
    }

    public void onCancel() {
        getComponent().dispose();
    }

    public void onRefresh() {
        getComponent().getBar().load();
        getComponent().getBtnOk().setEnabled(false);
        getComponent().getTable().setEnabled(false);
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    logger.info("Loading scripts...");
                    CollectiveScriptLoader.getInstance().load(new ScriptLoaderCallback() {
                        @Override
                        public void onCompletion(List<AgnosticStub<AbstractScript, ScriptManifest>> results) {
                            getComponent().setAvailableScripts(results);
                            getComponent().getBar().stop();
                            getComponent().getBtnOk().setEnabled(true);
                            getComponent().getTable().setEnabled(true);
                            getComponent().reinitializeTable();
                            logger.info("Done.");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    public void onBrowse() {
        try {
            Desktop.getDesktop().browse(new URL("http://vinsert.org").toURI());
        } catch (Exception e) {
            logger.error("Failed to open SDN in browser..", e);
        }
    }
}
