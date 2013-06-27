/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import net.engio.mbassy.bus.MBassador;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.message.MessageRecieve;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveTopic
    extends MessageRecieve
{
    public static final String COMMAND = "TOPIC";

    private final String channel;

    private final String sender;

    private final String topic;

    public ReceiveTopic( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );

        sender = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        channel = message.getParams()[0];
        topic = message.getParams()[1].substring( 1 );
    }

    // public ReceiveTopic( MBassador<IrcEvent> bus, String prefix, String channel, String topic )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, channel, topic ) );
    // }

    public String getChannel()
    {
        return channel;
    }

    public String getSender()
    {
        return sender;
    }

    public String getTopic()
    {
        return topic;
    }
}
