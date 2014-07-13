package org.vinsert.game;

import org.vinsert.core.Session;
import org.vinsert.core.StaticClassLoader;
import org.vinsert.game.exception.ConfigurationFailedException;
import org.vinsert.game.impl.FakeAppletStub;
import sun.applet.AppletClassLoader;

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;

/**
 * A loader class responsible for loading
 * the Oldschool RuneScape applet.
 */
public final class AppletLoader implements Callable<Applet> {
    private final AppletConfiguration configuration;
    private final FakeAppletStub appletStub;
    private final Session session;
    private Applet applet;

    public AppletLoader(Session session) {
        this.session = session;
        configuration = new AppletConfiguration();
        appletStub = new FakeAppletStub(configuration);
        try {
            configuration.load();
        } catch (IOException e) {
            throw new ConfigurationFailedException(e);
        }
    }

    @Override
    public Applet call() throws Exception {
        try {
            String mainClass = configuration.getMainClass();
            StaticClassLoader.prepare();
            URL jarURL = new URL("http://vinsert.org/metadata/gamepack.jar");
            URLClassLoader classLoader = AppletClassLoader.newInstance(new URL[]{jarURL});
            Class<?> client = classLoader.loadClass(mainClass);
            applet = (Applet) client.newInstance();
            appletStub.getAppletContext().setApplet(applet);
            applet.setStub(appletStub);
            applet.init();
            applet.start();
            session.onAppletLoaded();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
        return applet;
    }

    public AppletConfiguration getConfiguration() {
        return configuration;
    }

    public FakeAppletStub getAppletStub() {
        return appletStub;
    }

    public Applet getApplet() {
        return applet;
    }
}
