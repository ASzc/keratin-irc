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

public class ReceiveMode
    extends MessageRecieve
{
    public static final String COMMAND = "MODE";

    private final String target;

    private final String sender;

    private final String flags;

    public ReceiveMode( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );

        sender = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        target = message.getParams()[0];
        flags = message.getParams()[1];
    }

    // public ReceiveMode( MBassador<IrcEvent> bus, String prefix, String name, String mode )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, name, mode ));
    // }

    public String getTarget()
    {
        return target;
    }

    public String getSender()
    {
        return sender;
    }

    public String getFlags()
    {
        return flags;
    }
}
