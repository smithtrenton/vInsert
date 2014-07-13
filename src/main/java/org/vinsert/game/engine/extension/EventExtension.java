package org.vinsert.game.engine.extension;

import java.awt.event.*;

/**
 * Event extension for the client's mouse/movement/focus listeners.
 */
public abstract class EventExtension implements MouseListener, MouseMotionListener, FocusListener {
    private boolean acceptingEvents = true;
    private int mouseX, mouseY = 0;
    private AcceptanceListener acceptanceListener;

    /**
     * Utility method to make it easier to dispatch a MouseEvent.
     *
     * @param event MouseEvent to dispatch to the Clients MouseListener.
     */
    public final void dispatchEvent(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        switch (event.getID()) {
            case MouseEvent.MOUSE_MOVED:
                _mouseMoved(event);
                break;
            case MouseEvent.MOUSE_CLICKED:
                _mouseClicked(event);
                break;
            case MouseEvent.MOUSE_PRESSED:
                _mousePressed(event);
                break;
            case MouseEvent.MOUSE_RELEASED:
                _mouseReleased(event);
                break;
            case MouseEvent.MOUSE_EXITED:
                _mouseExited(event);
                break;
            case MouseEvent.MOUSE_ENTERED:
                _mouseEntered(event);
                break;
            case MouseEvent.MOUSE_DRAGGED:
                _mouseDragged(event);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseClicked(MouseEvent e) {
        if (acceptingEvents) {
            _mouseClicked(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mousePressed(MouseEvent e) {
        if (acceptingEvents) {
            _mousePressed(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseReleased(MouseEvent e) {
        if (acceptingEvents) {
            _mouseReleased(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseEntered(MouseEvent e) {
        if (acceptingEvents) {
            _mouseEntered(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseExited(MouseEvent e) {
        if (acceptingEvents) {
            _mouseExited(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseDragged(MouseEvent e) {
        if (acceptingEvents) {
            _mouseDragged(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseMoved(MouseEvent e) {
        if (acceptingEvents) {
            this.mouseX = e.getX();
            this.mouseY = e.getY();
            _mouseMoved(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void focusGained(FocusEvent e) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void focusLost(FocusEvent e) {
    }

    /**
     * Check to see if the client is currently
     * accepting mouse events or not.
     *
     * @return events
     */
    public final boolean isAcceptingEvents() {
        return acceptingEvents;
    }

    /**
     * Sets the flag for listener to accept
     * or deny mouse events.
     *
     * @param acceptingEvents true if accepting
     */
    public final void setAcceptingEvents(boolean acceptingEvents) {
        if (acceptanceListener != null) {
            acceptanceListener.setAcceptingEvents(acceptingEvents);
        }
        this.acceptingEvents = acceptingEvents;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public abstract void _mouseClicked(MouseEvent e);

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public abstract void _mousePressed(MouseEvent e);

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public abstract void _mouseReleased(MouseEvent e);

    /**
     * Invoked when the mouse enters a component.
     */
    public abstract void _mouseEntered(MouseEvent e);

    /**
     * Invoked when the mouse exits a component.
     */
    public abstract void _mouseExited(MouseEvent e);

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p/>
     * Due to platform-dependent Drag&Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&Drop operation.
     */
    public abstract void _mouseDragged(MouseEvent e);

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     */
    public abstract void _mouseMoved(MouseEvent e);


    public final int getMouseX() {
        return mouseX;
    }

    public final int getMouseY() {
        return mouseY;
    }

    public final AcceptanceListener getAcceptanceListener() {
        return acceptanceListener;
    }

    public final void setAcceptanceListener(AcceptanceListener acceptanceListener) {
        this.acceptanceListener = acceptanceListener;
    }

    public interface AcceptanceListener {

        void setAcceptingEvents(boolean eh);

    }
}
