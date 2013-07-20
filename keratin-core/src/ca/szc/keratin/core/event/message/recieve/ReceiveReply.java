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

public class ReceiveReply
    extends MessageReceive
{
    // Reply is a special case, encompassing all messages with numeric command parts

    public ReceiveReply( BlockingQueue<IrcMessage> replyQueue, IrcMessage message )
    {
        super( replyQueue, message );
    }
}
