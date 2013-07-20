/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.send;

import java.util.concurrent.BlockingQueue;

import ca.szc.keratin.core.event.message.MessageSend;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class SendPing
    extends MessageSend
{
    public static final String COMMAND = "PING";

    public SendPing( BlockingQueue<IrcMessage> replyQueue, String[] params )
        throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    {
        // TODO validation
        super( replyQueue, new IrcMessage( null, COMMAND, params ) );
    }

    public SendPing( BlockingQueue<IrcMessage> replyQueue, String server1, String server2 )
        throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    {
        // TODO validation
        super( replyQueue, new IrcMessage( null, COMMAND, server1, server2 ) );
    }

    public SendPing( BlockingQueue<IrcMessage> replyQueue, String server1 )
        throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    {
        super( replyQueue, new IrcMessage( null, COMMAND, server1 ) );
    }
}
