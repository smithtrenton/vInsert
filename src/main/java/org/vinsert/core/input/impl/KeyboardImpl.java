package org.vinsert.core.input.impl;

import com.google.inject.Inject;
import org.vinsert.api.Keyboard;
import org.vinsert.api.MethodContext;
import org.vinsert.api.util.Utilities;
import org.vinsert.game.engine.extension.KeyboardExtension;

import java.awt.event.KeyEvent;

/**
 * Date: 29/08/13
 * Time: 09:38
 *
 * @author Matt Collinge
 */
public final class KeyboardImpl implements Keyboard {

    private final MethodContext ctx;

    @Inject
    public KeyboardImpl(MethodContext context) {
        ctx = context;
    }

    public KeyboardExtension getKeyboard() {
        return (KeyboardExtension) ctx.client.getKeyboard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void type(String string) {
        type(string, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void type(String string, boolean enter) {
        KeyboardInputChain keyboardInputChain = new KeyboardInputChain(ctx);
        for (char character : string.toCharArray()) {
            keyboardInputChain.type(character);
        }
        if (enter) {
            keyboardInputChain.hold('\n');
            keyboardInputChain.release('\n');
        }
        dispatchChain(keyboardInputChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void type(char character) {
        KeyboardInputChain keyboardInputChain = new KeyboardInputChain(ctx);
        keyboardInputChain.type(character);
        dispatchChain(keyboardInputChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void type(int keyCode) {
        type((char) keyCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hold(char character) {
        KeyboardInputChain keyboardInputChain = new KeyboardInputChain(ctx);
        keyboardInputChain.hold(character);
        dispatchChain(keyboardInputChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hold(int keyCode) {
        hold((char) keyCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(char character) {
        KeyboardInputChain keyboardInputChain = new KeyboardInputChain(ctx);
        keyboardInputChain.release(character);
        dispatchChain(keyboardInputChain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(int keyCode) {
        release((char) keyCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void press(char character) {
        hold(character);
        release(character);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void press(int keycode) {
        hold(keycode);
        release(keycode);
    }

    /**
     * Utility method to dispatch a chain to the Clients KeyListeners.
     *
     * @param chain chain to dispatch to the Client KeyListeners.
     */
    private void dispatchChain(KeyboardInputChain chain) {
        java.util.List<KeyEvent> events = chain.getEvents();
        int[] sleepTimes = chain.getSleepTimes();
        for (int i = 0; i < events.size() && !Thread.currentThread().isInterrupted(); i++) {
            getKeyboard().dispatchEvent(events.get(i));
            Utilities.sleep(sleepTimes[i]);
        }
    }

}
