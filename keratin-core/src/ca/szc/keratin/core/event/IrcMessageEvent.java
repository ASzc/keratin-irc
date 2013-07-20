/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event;

import java.util.concurrent.BlockingQueue;

import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * An IRC event about a protocol message
 */
public abstract class IrcMessageEvent
    extends IrcEvent
{

    private final IrcMessage message;

    public IrcMessageEvent( BlockingQueue<IrcMessage> replyQueue, IrcMessage message )
    {
        super( replyQueue );
        this.message = message;
    }

    /**
     * Get the IRC message
     */
    public IrcMessage getMessage()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return message.toString();
    }

}
