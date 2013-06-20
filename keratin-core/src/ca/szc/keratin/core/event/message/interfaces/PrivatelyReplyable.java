/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.interfaces;

public interface PrivatelyReplyable
{
    /**
     * Reply to the event's sender privately with some text.
     * 
     * @param reply Text to use in reply
     */
    public void replyPrivately( String reply );
}
