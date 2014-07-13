package org.vinsert.game.exception;

/**
 * An exception thrown when something goes wrong with loading an applet resource.
 */
public class ResourceException extends RuntimeException {

    public ResourceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
