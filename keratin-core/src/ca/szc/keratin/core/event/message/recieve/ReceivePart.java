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

public class ReceivePart
    extends MessageRecieve
{
    public static final String COMMAND = "PART";

    private final String channel;

    private final String parter;

    public ReceivePart( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );

        parter = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        channel = message.getParams()[0].substring( 1 );
    }

    // public ReceivePart( MBassador<IrcEvent> bus, String prefix, String channel )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, channel ) );
    // }

    public String getParter()
    {
        return parter;
    }

    public String getChannel()
    {
        return channel;
    }

}
