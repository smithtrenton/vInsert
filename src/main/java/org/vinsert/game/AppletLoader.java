package org.vinsert.game;

import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.Logger;
import org.vinsert.core.Session;
import org.vinsert.game.exception.ConfigurationFailedException;
import org.vinsert.game.impl.FakeAppletStub;
import org.vinsert.util.Configuration;
import sun.applet.AppletClassLoader;

import java.applet.Applet;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;

/**
 * A loader class responsible for loading
 * the Oldschool RuneScape applet.
 */
public final class AppletLoader implements Callable<Applet> {
    private static final Logger logger = Logger.getLogger(AppletLoader.class);
    private final AppletConfiguration configuration;
    private final FakeAppletStub appletStub;
    private final Session session;
    private Applet applet;
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_" +
            "9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";
    private static final String PACK_URL = "http://vinsert.org/metadata/grab.php";

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
            //  StaticClassLoader.prepare();
            File jar = grab();
            URL jarURL = jar.toURI().toURL();
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

    private int currentVersion(int old) {
        int version = old;
        try {
            while (version < 200) {
                Socket socket = new Socket(configuration.getDocumentBase().replaceAll("http://", "").replaceAll("/", ""), 43594);
                InputStream socketInput = socket.getInputStream();
                OutputStream socketOutput = socket.getOutputStream();
                socketOutput.write(15);
                socketOutput.write(0);
                socketOutput.write(0);
                socketOutput.write(version >> 8);
                socketOutput.write(version);
                socketOutput.flush();
                int response = socketInput.read();
                if (response == 0) {
                    socket.close();
                    return version;
                } else {
                    socket.close();
                    version++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private File grab() throws IOException {
        int oldVersion = 50;
        for (File f : new File(Configuration.SETTINGS_DIR).listFiles()) {
            try {
                if (f.getName().endsWith(".jar")) {
                    String ver = f.getName().split("k|.jar")[1];
                    oldVersion = Integer.parseInt(ver);
                    break;
                }
            } catch (Exception e) {
            }
        }
        int version = currentVersion(oldVersion);
        if (version == -1) {
            logger.error("Version is -1");
        }
        File file = new File(Configuration.SETTINGS_DIR + "gamepack" + version + ".jar");
        if (file.exists()) {
            logger.info("Using cached gamepack");
            return file;
        }
        Content content = Request.Get(PACK_URL)
                .version(HttpVersion.HTTP_1_1)
                .userAgent(USER_AGENT)
                .useExpectContinue()
                .execute()
                .returnContent();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.asBytes());
        fos.close();
        return file;
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
