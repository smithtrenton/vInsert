package org.vinsert.core.exception;

/**
 * A generic exception that's thrown by the Container class.
 */
public class ContainerException extends RuntimeException {

    public ContainerException(String message) {
        super(message);
    }

    public ContainerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
