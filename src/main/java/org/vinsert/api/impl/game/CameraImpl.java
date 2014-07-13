package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.Camera;
import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Player;
import org.vinsert.api.wrappers.Tile;
import org.vinsert.api.wrappers.interaction.SceneNode;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * API class for interacting with the game camera.
 *
 * @author A_C/Cov
 * @author tobiewarburton
 * @see org.vinsert.api.Camera
 */
public final class CameraImpl implements Camera {
    private static final double DEGREES_PER_PIXEL_X = 0.35;
    private static final double DEGREES_PER_PIXEL_Y = 0.39;

    private final MethodContext context;

    @Inject
    public CameraImpl(MethodContext context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getX() {
        return context.client.getCameraX();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getY() {
        return context.client.getCameraY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getZ() {
        return context.client.getCameraZ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAngle() {
        return (int) (((double) context.client.getCameraYaw() / 2048.0) * 360.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPitch() {
        return context.client.getCameraPitch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPitch(final int pitch) {
        final int direction = getPitch() > pitch ? KeyEvent.VK_DOWN : KeyEvent.VK_UP;
        context.keyboard.hold(direction);
        Utilities.sleepUntil(new StatePredicate() {
            @Override
            public boolean apply() {
                return getPitch() >= pitch && direction == KeyEvent.VK_UP ||
                        getPitch() <= pitch && direction == KeyEvent.VK_DOWN;
            }
        }, 3000);
        context.keyboard.release(direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCompass(char dir) {
        switch (Character.toLowerCase(dir)) {
            case 'n':
                setAngle(359);
                break;
            case 'e':
                setAngle(89);
                break;
            case 's':
                setAngle(179);
                break;
            case 'w':
                setAngle(269);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAngle(int degrees) {
        int dir = KeyEvent.VK_LEFT;
        int start = getAngle();
        start = start < 180 ? start + 360 : start;
        degrees = degrees < 180 ? degrees + 360 : degrees;
        if (degrees > start && degrees - 180 < start) {
            dir = KeyEvent.VK_RIGHT;
        } else if (start > degrees && start - 180 >= degrees) {
            dir = KeyEvent.VK_RIGHT;
        }
        degrees %= 360;
        context.keyboard.hold(dir);
        int timeWaited = 0;
        int turnTime = 0;
        while ((getAngle() > degrees + 5 || getAngle() < degrees - 5) && turnTime < 6000) {
            Utilities.sleep(10);
            turnTime += 10;
            timeWaited += 10;
            if (timeWaited > 500) {
                int time = timeWaited - 500;
                if (time == 0 || time % 40 == 0) {
                    context.keyboard.hold(dir);
                }
            }
        }
        context.keyboard.release(dir);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPitch(final boolean up) {
        context.keyboard.hold(up ? KeyEvent.VK_UP : KeyEvent.VK_DOWN);
        Utilities.sleepUntil(new StatePredicate() {
            @Override
            public boolean apply() {
                return getPitch() == (up ? 383 : 128);
            }
        }, 3000);
        context.keyboard.release(up ? KeyEvent.VK_UP : KeyEvent.VK_DOWN);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getAngleTo(Tile tile) {
        Player local = context.players.getLocal();
        int x = local.getX() - tile.getX();
        int y = local.getY() - tile.getY();
        double angle = Math.toDegrees(Math.atan2(x, y));
        if (x == 0 && y > 0) {
            angle = 180;
        } else if (x < 0 && y == 0) {
            angle = 90;
        } else if (x == 0 && y < 0) {
            angle = 0;
        } else if (x < 0 && y == 0) {
            angle = 270;
        }

        if (x < 0 && y > 0) {
            angle += 270;
        } else if (x > 0 && y > 0) {
            angle += 90;
        } else if (x < 0 && y < 0) {
            angle = Math.abs(angle) - 180;
        } else if (x > 0 && y < 0) {
            angle = Math.abs(angle) + 270;
        }

        if (angle < 0) {
            angle = 360 + angle;
        } else if (angle >= 360) {
            angle -= 360;
        }
        return (int) angle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void turnTo(SceneNode node, int deviation) {
        int angle = getAngleTo(node.getTile());
        angle = Utilities.random(angle - deviation, angle + deviation + 1);
        setAngle(angle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void turnTo(SceneNode node) {
        setAngle(getAngleTo(node.getTile()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAngleM(int degrees) {
        degrees %= 360;
        int angleTo = degrees - getAngle();
        long start = System.currentTimeMillis();
        while (Math.abs(angleTo) > 5) {
            if (System.currentTimeMillis() - start > 6000) {
                break;
            }
            angleTo = degrees - getAngle();
            int pixelsTo = (int) Math.abs(angleTo / DEGREES_PER_PIXEL_X);
            if (pixelsTo > 450) {
                pixelsTo = pixelsTo / 450 * 450;
            }
            int startY = Utilities.random(-85, 85) + 200;
            if (angleTo > 0) {//right
                int startX = (500 - pixelsTo) - Utilities.random(0, 500 - pixelsTo - 10);
                drag(startX, startY, startX + pixelsTo, startY + Utilities.random(-10, 10));
            } else if (angleTo < 0) {//left
                int startX = (pixelsTo + 10) + Utilities.random(0, 500 - pixelsTo + 10);
                drag(startX, startY, startX - pixelsTo, startY + Utilities.random(-10, 10));
            }
        }
    }

    private void drag(int x, int y, int dragX, int dragY) {
        context.mouse.move(x, y);
        context.mouse.drag(MouseEvent.BUTTON2, dragX, dragY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPitchM(int pitch) {
        long start = System.currentTimeMillis();
        while (Math.abs(getPitch() - pitch) > 5) {
            if (System.currentTimeMillis() - start > 6000) {
                break;
            }
            boolean up = getPitch() < pitch;
            int startX = Utilities.random(-200, 200) + 250;
            int pixels = (int) (Math.abs(getPitch() - pitch) / DEGREES_PER_PIXEL_Y);
            if (pixels > 270) {
                pixels = pixels / 270 * 270;
            }

            if (up) {
                int startY = (300 - pixels - 10) - Utilities.random(0, 300 - pixels - 65);
                drag(startX, startY, startX + Utilities.random(-10, 10), startY + pixels);
            } else {
                int startY = (60 + pixels + 10) + Utilities.random(0, 300 - pixels - 70);
                drag(startX, startY, startX + Utilities.random(-10, 10), startY - pixels);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPitchM(boolean up) {
        setPitchM(up ? 383 : 128);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void turnToM(SceneNode node, int deviation) {
        int angle = getAngleTo(node.getTile());
        angle = Utilities.random(angle - deviation, angle + deviation + 1);
        setAngleM(angle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void turnToM(SceneNode node) {
        setAngleM(getAngleTo(node.getTile()));
    }
}
