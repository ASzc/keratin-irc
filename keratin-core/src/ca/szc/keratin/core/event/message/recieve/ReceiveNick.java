/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import java.util.concurrent.BlockingQueue;

import ca.szc.keratin.core.event.message.MessageReceive;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveNick
    extends MessageReceive
{
    public static final String COMMAND = "NICK";

    private final String nick;

    private final String sender;

    public ReceiveNick( BlockingQueue<IrcMessage> replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        sender = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        nick = message.getParams()[0];
    }

    public String getNick()
    {
        return nick;
    }

    public String getSender()
    {
        return sender;
    }
}
