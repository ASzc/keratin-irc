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
 * Socket has connected
 */
public class IrcConnect
    extends IrcConnectionEvent
{

    public IrcConnect( MBassador<IrcEvent> bus, Socket socket )
    {
        super( bus, socket );
    }

}
