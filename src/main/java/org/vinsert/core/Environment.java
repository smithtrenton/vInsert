package org.vinsert.core;

import org.vinsert.api.event.Events;
import org.vinsert.core.event.EnvironmentChangedEvent;
import org.vinsert.core.model.Account;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.component.tab.AbstractTab;
import org.vinsert.gui.component.tab.BotTab;
import org.vinsert.gui.controller.ConsoleController;
import org.vinsert.gui.controller.MainController;

import java.io.Serializable;

/**
 * A serializable session environment containing all
 * of the session settings, allowing it to be persisted
 * and started up again later on.
 * <p/>
 * TODO: Is this a good way to implement this stuff?
 */
public final class Environment implements Serializable {
    private final Events eventBus;
    private String name = "Unnamed";
    private Account account = null;
    private boolean useAccountName = false;
    private boolean synchronizedClock = false;
    private int framesPerSecond = 30;
    private boolean render = true;
    private Session session;
    private boolean breakingEnabled = false;

    public Environment() {
        this.eventBus = new Events(this);
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private void propertyChanged(String name, Object old, Object now) {
        eventBus.submit(new EnvironmentChangedEvent(name, old, now));
        if (now instanceof Account) {
            MainController controller = ControllerManager.get(MainController.class);
            for (AbstractTab tab : controller.getComponent().getTabs()) {
                if (tab instanceof BotTab && ((BotTab) tab).getSession().getEnvironment().equals(this)) {
                    tab.setToolTipText(((Account) now).getUsername());
                    ConsoleController controller1 = ControllerManager.get(ConsoleController.class);
                    if (controller1.getSession() != null && controller1.getSession().equals(((BotTab) tab).getSession())) {
                        controller1.setName(((Account) now).getUsername());
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        propertyChanged("name", this.name, name);
        this.name = name;
    }

    /**
     * NOTE: This is PACKAGE PRIVATE for a reason.
     *
     * @return helo
     */
    Account getAccount() {
        return account;
    }

    /**
     * Check to see if theres an account in the
     * environment or not.
     *
     * @return true if there is
     */
    public boolean hasAccount() {
        return account != null;
    }

    public Session getSession() {
        return session;
    }

    public void setAccount(Account account) {
        propertyChanged("account", this.account, account);
        this.account = account;
    }

    public boolean isUseAccountName() {
        return useAccountName;
    }

    public void setUseAccountName(boolean useAccountName) {
        propertyChanged("useAccountName", this.useAccountName, useAccountName);
        this.useAccountName = useAccountName;
    }

    public boolean isSynchronizedClock() {
        return synchronizedClock;
    }

    /**
     * Sets a flag telling the session to synchronize with the
     * clients loopcycle or use a generated pulse.
     *
     * @param synchronizedClock true to sync with client,
     *                          false for generated pulse.
     */
    public void setSynchronizedClock(boolean synchronizedClock) {
        propertyChanged("synchronizedClock", this.synchronizedClock, synchronizedClock);
        this.synchronizedClock = synchronizedClock;
    }

    public boolean canRender() {
        return render;
    }

    public void setRender(boolean val) {
        render = val;
    }

    public Events getEventBus() {
        return eventBus;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    public void setFramesPerSecond(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
    }

    public boolean isBreakingEnabled() {
        return breakingEnabled;
    }

    public void setBreakingEnabled(boolean breakingEnabled) {
        propertyChanged("breakingEnabled", this.breakingEnabled, breakingEnabled);
        this.breakingEnabled = breakingEnabled;
    }
}
