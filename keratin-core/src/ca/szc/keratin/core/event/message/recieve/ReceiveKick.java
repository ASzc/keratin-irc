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

public class ReceiveKick
    extends MessageReceive
{
    public static final String COMMAND = "KICK";

    private final String channel;

    private final String sender;

    private final String target;

    public ReceiveKick( BlockingQueue<IrcMessage> replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        sender = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        channel = message.getParams()[0];
        target = message.getParams()[1];
    }

    public String getChannel()
    {
        return channel;
    }

    public String getSender()
    {
        return sender;
    }

    public String getTarget()
    {
        return target;
    }
}
