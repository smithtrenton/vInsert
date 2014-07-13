package org.vinsert.gui.component.tab;

import org.vinsert.core.Session;
import org.vinsert.gui.icons.Icons;
import org.vinsert.gui.view.AppletView;

import javax.swing.*;
import java.awt.*;

public final class BotTab extends AbstractTab {
    private AppletView appletView;
    private String name;
    private Session session;

    public BotTab(String name) {
        //      super(name);
        this.name = name;
        setToolTipText(name);
        init();
        setIcon(Icons.USER);
    }

    public JPanel getPanel() {
        return this.appletView;
    }

    public Session getSession() {
        return session;
    }

    private void init() {
        this.appletView = new AppletView();
        SwingWorker worker = new SwingWorker() {
            protected Object doInBackground() throws Exception {
                BotTab.this.session = new Session();
                try {
                    session.getAppletLoader().call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                appletView.setApplet(session.getAppletLoader().getApplet());
                EventQueue.invokeLater(appletUpdaterRunnable());
                return null;
            }
        };
        worker.execute();
    }

    private Runnable appletUpdaterRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                appletView.update();
            }
        };
    }

    public String getName() {
        return this.name;
    }
}
