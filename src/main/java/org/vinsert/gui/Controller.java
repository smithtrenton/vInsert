package org.vinsert.gui;

/**
 * @author : const_
 */
public abstract class Controller<T> {

    public abstract boolean isComponentInitiated();

    public abstract T getComponent();

}
