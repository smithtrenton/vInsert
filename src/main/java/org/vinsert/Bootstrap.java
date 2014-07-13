package org.vinsert;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Bootstrap loader class that adds some JVM arguments.
 */
public final class Bootstrap {
    private static Logger logger = Logger.getLogger(Bootstrap.class);

    private Bootstrap() {

    }

    public static void main(String[] args) throws Exception {
        StringBuilder jvmArgs = new StringBuilder("-XX:+UseParNewGC -XX:MinHeapFreeRatio=5 -XX:MaxHeapFreeRatio=10");
        for (String arg : args) {
            if (arg.startsWith("maxHeap=")) {
                logger.info("Max heap size: " + arg.substring(arg.indexOf('='), arg.length()));
                jvmArgs.append(" -Xmx").append(arg.substring(arg.indexOf('='), arg.length()));
            }
        }
        String cmd = new StringBuilder().append("java -cp \"").append(System.getProperty("java.class.path"))
                .append("\" ").append(jvmArgs).append(" ").append(Application.class.getCanonicalName()).toString();
        Runtime runtime = Runtime.getRuntime();
        Process proc;
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            proc = runtime.exec(new String[]{"/bin/sh", "-c", cmd});
            logger.info("Started process by command " + cmd);
        } else {
            proc = runtime.exec(cmd);
        }
        final Process process = proc;
        new Thread(new InputStreamConsumer(process.getInputStream())).start();
        new Thread(new InputStreamConsumer(process.getErrorStream())).start();
    }

    private static class InputStreamConsumer implements Runnable {
        private final InputStream stream;

        public InputStreamConsumer(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public void run() {
            final BufferedReader input = new BufferedReader(new
                    InputStreamReader(stream));
            String buf = "";
            try {
                while ((buf = input.readLine()) != null) {
                    System.out.println(buf);
                    Thread.sleep(50);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
