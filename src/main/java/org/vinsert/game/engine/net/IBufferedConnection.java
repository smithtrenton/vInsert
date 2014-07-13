package org.vinsert.game.engine.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 */
public interface IBufferedConnection {


    Socket getSocket();


    OutputStream getOutput();


    InputStream getInput();


    IBuffer getBuffer();

}
