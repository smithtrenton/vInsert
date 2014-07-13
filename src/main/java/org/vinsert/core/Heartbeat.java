package org.vinsert.core;

import org.vinsert.api.event.Events;
import org.vinsert.core.event.PulseEvent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * The "heartbeat" of the bot. A thread that pushes a tick event
 * to all subscribers of the event bus in the session environment.
 */
public final class Heartbeat implements Runnable {
    private final ScheduledExecutorService scheduler;
    private final Session session;

    public Heartbeat(Session session) {
        int procs = Runtime.getRuntime().availableProcessors();
        this.scheduler = newScheduledThreadPool(1 + (procs / 2));
        this.session = session;
    }

    @Override
    public void run() {
        try {
            Environment env = session.getEnvironment();
            Events eventBus = env.getEventBus();
            if (session.getState() == Session.State.ACTIVE) {
                eventBus.submit(new PulseEvent(currentTimeMillis()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        schedule(this, 600);
    }

    public void stop() {
        try {
            scheduler.awaitTermination(10000, TimeUnit.MILLISECONDS);
            scheduler.shutdown();
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    public void schedule(Runnable runnable, int every) {
        scheduler.scheduleAtFixedRate(runnable, 0, every, TimeUnit.MILLISECONDS);
    }

    public boolean isRunning() {
        return !scheduler.isShutdown();
    }
}
