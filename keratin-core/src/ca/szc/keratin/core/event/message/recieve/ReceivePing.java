/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import java.util.concurrent.BlockingQueue;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.message.MessageReceive;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceivePing
    extends MessageReceive
{
    public static final String COMMAND = "PING";

    private final String[] params;

    public ReceivePing( BlockingQueue<IrcMessage> replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        params = message.getParams();
    }

    public String[] getParams()
    {
        return params;
    }

    public void pong()
    {
        try
        {
            getReplyQueue().offer( new IrcMessage( null, "PING", params ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Error creating reply message" );
        }
    }
}
