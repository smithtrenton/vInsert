package org.vinsert.api.event;

/**
 *
 */
public final class MouseClickEvent {
    private int x;
    private int y;
    private int button;

    public MouseClickEvent(int x, int y, int button) {
        this.x = x;
        this.y = y;
        this.button = button;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }
}
