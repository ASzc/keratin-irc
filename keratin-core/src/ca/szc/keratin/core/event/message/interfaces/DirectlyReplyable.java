/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.interfaces;

public interface DirectlyReplyable
{
    /**
     * Reply to the event with some text by appending the sender to the reply message.
     * 
     * @param reply Text to use in reply
     */
    public void replyDirectly( String reply );
}
