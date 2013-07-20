/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event;

import ca.szc.keratin.core.net.io.OutputQueue;

/**
 * An event tied to some part of IRC
 */
public abstract class IrcEvent
{

    private final OutputQueue replyQueue;

    /**
     * Reference to the queue supplied at instantiation to handle reply messages.
     * 
     * @return The supplied queue for reply messages.
     */
    public OutputQueue getReplyQueue()
    {
        return replyQueue;
    }

    public IrcEvent( OutputQueue replyQueue )
    {
        this.replyQueue = replyQueue;
    }

}
