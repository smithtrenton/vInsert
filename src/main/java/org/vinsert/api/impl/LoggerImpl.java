package org.vinsert.api.impl;

import com.google.inject.Inject;
import org.vinsert.api.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

/**
 * Logger implementation
 *
 * @author tommo
 */
public final class LoggerImpl implements Logger {

    /**
     * The log time format
     */
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("H:mm");

    /**
     * The maximum log queue size before it starts sliding
     */
    public static final int MAX_QUEUE_SIZE = 100;

    /**
     * Maximum number of logger entries to be fed to the view every second
     */
    public static final int MAX_THROUGHPUT_PER_SECOND = 20;

    /**
     * Log4j logger
     */
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LoggerImpl.class);

    /**
     * The logger entry message queue
     */
    private Queue<LoggerEntry> messageQueue = new ConcurrentLinkedQueue<LoggerEntry>();

    @Inject
    public LoggerImpl() {
        //(new Thread(new LogConsumer())).start();
    }

    @Override
    public void log(Object message) {
        log(Level.INFO, message);
    }

    @Override
    public void log(Level level, Object message) {
        if (messageQueue.size() > MAX_QUEUE_SIZE) {
            logger.debug("Message queue reached capacity of " + MAX_QUEUE_SIZE + ", sliding and throttling...");
            messageQueue.poll();
        }

        messageQueue.offer(new LoggerEntry(level, message));
    }

    @Override
    public void log(Level level, Object message, Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        for (StackTraceElement e : t.getStackTrace()) {
            sb.append("\n\t").append(e);
        }

        log(level, sb);
    }

    /**
     * Encapsulates a message and a severity level
     *
     * @author tommo
     */
    public static final class LoggerEntry {

        private final Date date;
        private final Level level;
        private final Object message;

        public LoggerEntry(final Level level, final Object message) {
            this.level = level;
            this.message = message;
            this.date = new Date();
        }

        public final Level getLevel() {
            return level;
        }

        public final Object getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return FORMAT.format(date) + " " + level.toString() + " " + message.toString();
        }

    }

}
