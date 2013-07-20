/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message;

import ca.szc.keratin.core.event.IrcMessageEvent;
import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * IRC messages being received
 */
public abstract class MessageReceive
    extends IrcMessageEvent
{

    public MessageReceive( OutputQueue replyQueue, IrcMessage message )
    {
        super( replyQueue, message );
    }

}