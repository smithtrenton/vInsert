package org.vinsert.core.input;

import java.awt.event.InputEvent;

/**
 * Date: 16/09/13
 * Time: 09:33
 *
 * @author Matt Collinge
 */
public interface IEventGenerator<T extends InputEvent> {

    T generate();

}
