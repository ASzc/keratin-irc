/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import ca.szc.keratin.core.event.message.MessageReceive;
import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceivePart
    extends MessageReceive
{
    public static final String COMMAND = "PART";

    private final String channel;

    private final String parter;

    private final String reason;

    public ReceivePart( OutputQueue replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        parter = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        channel = message.getParams()[0];
        reason = message.getParams()[1].substring( 1 );
    }

    public String getChannel()
    {
        return channel;
    }

    public String getParter()
    {
        return parter;
    }

    public String getReason()
    {
        return reason;
    }
}
