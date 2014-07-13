package org.vinsert.core.input.impl;

import org.vinsert.api.MethodContext;
import org.vinsert.api.util.Utilities;
import org.vinsert.core.input.InputChain;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 29/08/13
 * Time: 09:42
 *
 * @author Matt Collinge
 */
public final class KeyboardInputChain extends InputChain<KeyEvent> {

    private static final Map<Character, Character> SPECIAL_CHARS;

    public KeyboardInputChain(MethodContext context) {
        super(context);
    }

    /**
     * Adds a key type to the underlying List.
     *
     * @param keycode keycode of key to type.
     */
    public void type(int keycode) {
        Character newChar = SPECIAL_CHARS.get((char) keycode);
        char keyCode = (char) Character.toUpperCase((newChar == null) ? keycode : newChar);

        if (Character.isLowerCase(keycode) || (!Character.isLetter(keycode) && (newChar == null))) {
            addEvent(new KeyEvent(getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, (char) keycode, KeyEvent.KEY_LOCATION_STANDARD), 0);
            addEvent(new KeyEvent(getComponent(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, 0, (char) keycode), 0);
            addEvent(new KeyEvent(getComponent(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode, (char) keycode, KeyEvent.KEY_LOCATION_STANDARD), getSleep());
        } else {
            addEvent(new KeyEvent(getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.SHIFT_MASK, KeyEvent.VK_SHIFT, KeyEvent.CHAR_UNDEFINED), getSleep());
            addEvent(new KeyEvent(getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.SHIFT_MASK, keyCode, (char) keycode), 0);
            addEvent(new KeyEvent(getComponent(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), KeyEvent.SHIFT_MASK, 0, (char) keycode), 0);
            addEvent(new KeyEvent(getComponent(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), KeyEvent.SHIFT_MASK, keyCode, (char) keycode), 0);
            addEvent(new KeyEvent(getComponent(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SHIFT, KeyEvent.CHAR_UNDEFINED), getSleep());
        }
    }

    /**
     * Adds a key hold to the underlying list.
     *
     * @param keycode keycode of key to hold.
     */
    public void hold(int keycode) {
        addEvent(getSpecialKeyEvent(KeyEvent.KEY_PRESSED, keycode), getSleep());
    }

    /**
     * Adds a key release to the underlying List.
     *
     * @param keycode keycode of key to release.
     */
    public void release(int keycode) {
        addEvent(getSpecialKeyEvent(KeyEvent.KEY_RELEASED, keycode), getSleep());
    }

    /**
     * Generates a random sleep time for use when dispatching.
     *
     * @return sleep time.
     */
    private int getSleep() {
        return Utilities.random(15, 45);
    }

    /**
     * Generates a special KeyEvent to deal with shift, alt and ctrl key presses.
     *
     * @param type    type of KeyEvent to generate.
     * @param keycode keycode of key.
     * @return new KeyEvent with the appropriate key mask.
     */
    private KeyEvent getSpecialKeyEvent(int type, int keycode) {
        int modifier = 0;
        switch (keycode) {
            case KeyEvent.VK_SHIFT:
                modifier = KeyEvent.SHIFT_MASK;
                break;
            case KeyEvent.VK_ALT:
                modifier = KeyEvent.ALT_MASK;
                break;
            case KeyEvent.VK_CONTROL:
                modifier = KeyEvent.CTRL_MASK;
                break;
        }
        return new KeyEvent(getComponent(), type, System.currentTimeMillis(), type == KeyEvent.KEY_PRESSED ? modifier : 0, keycode, KeyEvent.CHAR_UNDEFINED);
    }

    /**
     *
     * Init of the special char replacement map.
     *
     */
    static {        //todo more dynamic: support dvorak, qwertz etc auto detection
        char[] spChars = {'~', '!', '@', '#', '%', '^', '&', '*', '(', ')', '_', '+', '{', '}', ':', '<', '>', '?', '"', '|'};
        char[] replace = {'`', '1', '2', '3', '5', '6', '7', '8', '9', '0', '-', '=', '[', ']', ';', ',', '.', '/', '\'', '\\'};
        SPECIAL_CHARS = new HashMap<>(spChars.length);
        for (int x = 0; x < spChars.length; ++x) {
            SPECIAL_CHARS.put(spChars[x], replace[x]);
        }
    }

}
