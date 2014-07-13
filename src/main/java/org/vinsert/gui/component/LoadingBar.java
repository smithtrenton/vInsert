package org.vinsert.gui.component;

import javax.swing.*;

/**
 * @author const_
 */
public final class LoadingBar extends JProgressBar {

    public void load() {
        setIndeterminate(true);
    }

    public void stop() {
        setIndeterminate(false);
        setValue(0);
    }
}
