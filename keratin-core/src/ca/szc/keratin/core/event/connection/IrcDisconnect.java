/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.connection;

import java.net.Socket;

import net.engio.mbassy.bus.MBassador;

import ca.szc.keratin.core.event.IrcConnectionEvent;
import ca.szc.keratin.core.event.IrcEvent;

/**
 * Socket has disconnected
 */
public class IrcDisconnect
    extends IrcConnectionEvent
{

    public IrcDisconnect( MBassador<IrcEvent> bus, Socket socket )
    {
        super( bus, socket );
    }

}
