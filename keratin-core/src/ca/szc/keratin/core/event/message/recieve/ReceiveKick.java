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

public class ReceiveKick
    extends MessageRecieve
{
    public static final String COMMAND = "KICK";

    private final String channel;

    private final String sender;

    private final String target;

    public ReceiveKick( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );

        sender = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        channel = message.getParams()[0];
        target = message.getParams()[1];
    }

    // public ReceiveKick( MBassador<IrcEvent> bus, String prefix, String channel, String user, String comment )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, channel, user, comment ) );
    // }

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
