package org.vinsert.api;

/**
 * Date: 29/08/13
 * Time: 09:26
 *
 * @author Matt Collinge
 */
public interface Keyboard {

    /**
     * Types the characters in game.
     *
     * @param string String to input.
     */
    void type(String string);

    /**
     * Types the characters in game and the presses enter.
     *
     * @param string String to input.
     * @param enter  true to press enter, else false.
     */
    void type(String string, boolean enter);

    /**
     * Types the character in game.
     *
     * @param character character to type.
     */
    void type(char character);

    /**
     * Types the character in game.
     *
     * @param keyCode character to type.
     */
    void type(int keyCode);

    /**
     * Hold down specified character.
     *
     * @param character char to hold down.
     */
    void hold(char character);

    /**
     * Hold down specified character.
     *
     * @param keyCode keycode of char to hold down.
     */
    void hold(int keyCode);

    /**
     * Release specified character.
     *
     * @param character char to release.
     */
    void release(char character);

    /**
     * Release specified character.
     *
     * @param keyCode keycode of char to release.
     */
    void release(int keyCode);

    /**
     * Press down a character without actually typing.
     *
     * @param character char to press.
     */
    void press(char character);

    /**
     * Press down a character without actually typing.
     *
     * @param keycode keycode of char to press.
     */
    void press(int keycode);

}
