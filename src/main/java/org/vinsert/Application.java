package org.vinsert;

import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;
import org.vinsert.core.CustomSecurityManager;
import org.vinsert.core.model.Account;
import org.vinsert.core.model.Property;
import org.vinsert.core.model.Source;
import org.vinsert.gui.ControllerManager;
import org.vinsert.gui.component.Appender;
import org.vinsert.gui.controller.*;
import org.vinsert.util.Configuration;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Application entry point
 */
public final class Application {
    private static Logger logger = Logger.getLogger(Application.class);
    public static final double VERSION = 4.02;

    private Application() {

    }

    public static void main(String[] args) {
        Configuration.mkdirs();
        Logger.getRootLogger().addAppender(new Appender(new SimpleLayout()));
        System.setSecurityManager(new CustomSecurityManager());
        prepareEnvironment();
        try {
            UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    new AccountController();
                    new ConditionCreatorController();
                    new MainController();
                    new ProfileController();
                    new ProfileLoaderController();
                    new ScriptController();
                    new SettingsDebugController();
                    new UnlockController();
                    new WidgetDebugController();
                    new ConsoleController();
                    ControllerManager.get(UnlockController.class).show();
                }
            });
        } catch (InterruptedException | InvocationTargetException | UnsupportedLookAndFeelException e) {
            logger.error("An error occured while initialising the UI", e);
        }
    }

    private static void prepareEnvironment() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Property.getContainer().save();
                Account.getContainer().save();
                Source.getContainer().save();
            }
        }));

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            Logger logger = Logger.getLogger("EXCEPTIONS");

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                logger.error("Exception on thread " + t.getName(), e);
            }
        });

        Property.getContainer().load();
        Property.defaults();
        Source.getContainer().load();
        Source.defaults();
        Account.getContainer().load();
        Account.defaults();
    }
}
