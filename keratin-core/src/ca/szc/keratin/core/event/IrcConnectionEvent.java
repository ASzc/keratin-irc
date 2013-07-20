/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * An event about a change in the IRC socket connection
 */
public abstract class IrcConnectionEvent
    extends IrcEvent
{

    private final Socket socket;

    public IrcConnectionEvent( BlockingQueue<IrcMessage> replyQueue, Socket socket )
    {
        super( replyQueue );
        this.socket = socket;
    }

    /**
     * Get the socket that the event pertains to.
     * 
     * @return The related Socket, or null if initial or final use of the socket.
     */
    public Socket getSocket()
    {
        return socket;
    }

    @Override
    public String toString()
    {
        return "IrcConnectionEvent [socket=" + socket + "]";
    }

}
