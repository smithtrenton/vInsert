package org.vinsert.core.input.impl;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Mouse;
import org.vinsert.api.util.DynamicPoint;
import org.vinsert.api.util.MouseCallback;
import org.vinsert.api.util.Utilities;
import org.vinsert.core.input.algorithm.impl.StraightLineGenerator;
import org.vinsert.core.input.algorithm.impl.ZetaMouseGenerator;
import org.vinsert.game.engine.extension.EventExtension;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Date: 29/08/13
 * Time: 09:36
 *
 * @author Matt Collinge
 */
public final class MouseImpl implements Mouse {
    private final ZetaMouseGenerator zetaMouseGenerator = new ZetaMouseGenerator();
    private final StraightLineGenerator straightLineGenerator = new StraightLineGenerator();
    private final MethodContext ctx;

    @Inject
    public MouseImpl(MethodContext context) {
        ctx = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void move(Point point) {
        MouseInputChain mouseInputChain = new MouseInputChain(ctx);
        mouseInputChain.path(getPath(randomize(point)));
        dispatchChain(mouseInputChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveQuick(Point point) {
        MouseInputChain mouseInputChain = new MouseInputChain(ctx);
        mouseInputChain.pathQuick(getPath(randomize(point)));
        dispatchChain(mouseInputChain);
    }

    @Override
    public boolean moveDynamic(DynamicPoint point, MouseCallback callback) {
        MouseInputChain chain = new MouseInputChain(ctx);
        int pointOffset = 0;
        Point last = point.getPoint();
        chain.path(getPath(randomize(last)));
        while (!Thread.currentThread().isInterrupted() && getPosition().distance(last) > 5) {
            Point curr = point.getPoint();
            if (curr.distance(last) > 1) {
                chain.path(getPath(randomize(curr)));
                last = point.getPoint();
            }
            java.util.List<MouseEvent> events = chain.getEvents();
            int[] sleepTimes = chain.getSleepTimes();
            if (pointOffset < events.size()) {
                getMouse().dispatchEvent(events.get(pointOffset));
                Utilities.sleep(sleepTimes[pointOffset]);
                pointOffset++;
            } else {
                return false;
            }
        }
        return callback.onComplete();
    }

    @Override
    public boolean moveDynamicQuick(DynamicPoint point, MouseCallback callback) {
        MouseInputChain chain = new MouseInputChain(ctx);
        int pointOffset = 0;
        Point last = point.getPoint();
        chain.pathQuick(getPath(randomize(last)));
        while (!Thread.currentThread().isInterrupted() && getPosition().distance(last) > 5) {
            Point curr = point.getPoint();
            if (curr.distance(last) > 1) {
                chain.pathQuick(getPath(randomize(curr)));
                last = point.getPoint();
            }
            java.util.List<MouseEvent> events = chain.getEvents();
            int[] sleepTimes = chain.getSleepTimes();
            if (pointOffset < events.size()) {
                getMouse().dispatchEvent(events.get(pointOffset));
                Utilities.sleep(sleepTimes[pointOffset]);
                pointOffset++;
            } else {
                return false;
            }
        }
        return callback.onComplete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void move(int x, int y) {
        move(new Point(x, y));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void click(boolean left) {
        MouseInputChain mouseInputChain = new MouseInputChain(ctx);
        mouseInputChain.click(getPosition(), left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3, 1);
        dispatchChain(mouseInputChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void click(Point point, boolean left) {
        Point randomizedTarget = randomize(point);
        MouseInputChain mouseInputChain = new MouseInputChain(ctx);
        mouseInputChain.path(getPath(randomizedTarget));
        dispatchChain(mouseInputChain);

        MouseInputChain clickChain = new MouseInputChain(ctx);
        clickChain.click(point, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3, 1);
        dispatchChain(clickChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void click(int x, int y, boolean left) {
        click(new Point(x, y), left);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void drag(int button, Point point) {
        Point randomizedTarget = randomize(point);
        MouseInputChain mouseInputChain = new MouseInputChain(ctx);
        mouseInputChain.drag(getPath(randomizedTarget), button);
        dispatchChain(mouseInputChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drag(int button, int x, int y) {
        drag(button, new Point(x, y));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drag(Point point) {
        drag(MouseEvent.BUTTON1, point);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void press(Point point) {
        MouseInputChain mouseInputChain = new MouseInputChain(ctx);
        mouseInputChain.press(point, MouseEvent.BUTTON1);
        dispatchChain(mouseInputChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Point point) {
        MouseInputChain mouseInputChain = new MouseInputChain(ctx);
        mouseInputChain.release(point, MouseEvent.BUTTON1);
        dispatchChain(mouseInputChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drag(int x, int y) {
        drag(new Point(x, y));
    }

    /**
     * Gets the clients implementation of the MouseListener.
     *
     * @return Clients MouseListener.
     */
    private EventExtension getMouse() {
        return (EventExtension) ctx.client.getMouse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getPosition() {
        return new Point(getMouse().getMouseX(), getMouse().getMouseY());
    }

    private Point randomize(Point point) {
        return new Point(
                Utilities.random(point.x - 2, point.x + 2),
                Utilities.random(point.y - 2, point.y + 2)
        );
    }

    /**
     * Generates either a straight path or curved path dependent on distance to target.
     *
     * @param point target Point.
     * @return Point array representing the path.
     */
    private Point[] getPath(Point point) {
        Point current = new Point(getMouse().getMouseX(), getMouse().getMouseY());
        double distance = Math.sqrt(Math.pow(current.x - point.x, 2) + Math.pow(current.y - point.y, 2));
        return distance <= 80
                ? straightLineGenerator.generate(current, point)
                : zetaMouseGenerator.generate(current, point);
    }

    /**
     * Utility method to dispatch a chain to the Clients MouseListener.
     *
     * @param chain chain to dispatch to the Client MouseListener.
     */
    private void dispatchChain(MouseInputChain chain) {
        java.util.List<MouseEvent> events = chain.getEvents();
        int[] sleepTimes = chain.getSleepTimes();
        for (int i = 0; i < events.size() && !Thread.currentThread().isInterrupted(); i++) {
            getMouse().dispatchEvent(events.get(i));
            if (sleepTimes[i] > 0) {
                Utilities.sleep(sleepTimes[i]);
            } else {
                try {
                    Thread.sleep(0, 500000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
        Utilities.sleep(60, 74);
    }

}
