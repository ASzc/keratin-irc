/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message;

import net.engio.mbassy.bus.MBassador;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.IrcMessageEvent;
import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * IRC messages being sent
 */
public abstract class MessageSend
    extends IrcMessageEvent
{

    public MessageSend( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );
    }

}
