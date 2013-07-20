/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message;

import java.util.concurrent.BlockingQueue;

import ca.szc.keratin.core.event.IrcMessageEvent;
import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * IRC messages being sent
 * 
 * @deprecated in favor of replyQueue
 */
public abstract class MessageSend
    extends IrcMessageEvent
{

    public MessageSend( BlockingQueue<IrcMessage> replyQueue, IrcMessage message )
    {
        super( replyQueue, message );
    }

}
