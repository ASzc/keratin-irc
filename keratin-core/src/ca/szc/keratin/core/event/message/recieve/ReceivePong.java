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

public class ReceivePong
    extends MessageRecieve
{
    public static final String COMMAND = "PONG";

    private final String[] params;

    public ReceivePong( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );

        params = message.getParams();
    }

    public String[] getParams()
    {
        return params;
    }
}
