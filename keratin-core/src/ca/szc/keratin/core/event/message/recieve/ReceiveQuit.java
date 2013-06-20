/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import net.engio.mbassy.bus.MBassador;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.message.MessageRecieve;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveQuit
    extends MessageRecieve
{
    public static final String COMMAND = "QUIT";

    public ReceiveQuit( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );
    }

    // public ReceiveQuit( MBassador<IrcEvent> bus, String prefix, String text )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, text ) );
    // }
}
