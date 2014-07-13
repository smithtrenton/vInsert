package org.vinsert.game.engine;

/**
 * @author const_
 */
public interface ILoginHandler {


    String getUsername();


    String getPassword();


    int getMenuIndex();


    int getCursorPos();


    void login(Object gameEngine);
}
