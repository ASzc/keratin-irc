/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.net;

import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.message.recieve.ReceivePing;

/**
 * Event handlers for connection events
 */
public class IrcConnectionHandlers
{
    @Handler
    private void handlePingPong( ReceivePing event )
    {
        Logger.trace( "Handling PING by sending echo PONG" );
        event.pong();
    }
}
