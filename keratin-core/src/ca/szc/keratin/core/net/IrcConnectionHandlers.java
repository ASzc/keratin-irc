/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.SocketFactory;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.event.connection.IrcDisconnect;
import ca.szc.keratin.core.event.message.recieve.ReceivePing;

/**
 * Event handlers for connection events
 */
public class IrcConnectionHandlers
{

    private static final int CHECK_TIME = 5000;

    private final SocketFactory socketFactory;

    private final InetSocketAddress endpoint;

    public IrcConnectionHandlers( InetSocketAddress endpoint, SocketFactory socketFactory )
    {
        Logger.trace( "IrcConnectionHandlers instantiation" );
        this.endpoint = endpoint;
        this.socketFactory = socketFactory;
    }

    @Handler
    private void reconnectHandler( IrcDisconnect event )
    {
        MBassador<IrcEvent> bus = event.getBus();

        Logger.trace( "Making sure the old socket is closed" );
        try
        {
            Socket socket = event.getSocket();
            if ( socket != null && !socket.isClosed() )
            {
                socket.close();
            }
        }
        catch ( IOException e )
        {
            Logger.trace( "Error when closing socket" );
        }

        Logger.info( "Attempting to reconnect" );
        newConnection( bus );
    }

    private void newConnection( MBassador<IrcEvent> bus )
    {
        while ( !Thread.interrupted() )
        {
            try
            {
                Logger.trace( "Creating/connecting socket" );
                Socket socket = socketFactory.createSocket( endpoint.getAddress(), endpoint.getPort() );
                Logger.info( "Successfully connected socket" );
                bus.publishAsync( new IrcConnect( bus, socket ) );
                break;
            }
            catch ( IOException e )
            {
                Logger.error( e, "Failed to connect socket, sleeping before retrying." );
                try
                {
                    Thread.sleep( CHECK_TIME );
                }
                catch ( InterruptedException e1 )
                {
                }
            }
        }
    }

    @Handler
    private void handlePingPong( ReceivePing event )
    {
        Logger.trace( "Handling PONG by sending echo PING" );
        event.pong();
    }
}
