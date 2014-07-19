package org.vinsert.util;

import java.io.File;

/**
 * @author : const_
 */
public class Configuration {

    public static final String HOME_DIR = System.getProperty("user.home") + File.separator + "vInsert";
    public static final String SCRIPTS_DIR = HOME_DIR + File.separator + "Scripts" + File.separator;
    public static final String SETTINGS_DIR = HOME_DIR + File.separator + "Settings" + File.separator;
    public static final String SCREENSHOTS_DIR = HOME_DIR + File.separator + "Screenshots" + File.separator;

    public static void mkdirs() {
        for (File f : new File[]{new File(HOME_DIR), new File(SCRIPTS_DIR), new File(SETTINGS_DIR),
                new File(SCREENSHOTS_DIR)}) {
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }
}
