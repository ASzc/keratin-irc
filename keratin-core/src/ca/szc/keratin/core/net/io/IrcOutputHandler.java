/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.net.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.event.message.MessageSend;
import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * Receives all MessageSend events and sends them out on the socket.
 */
public class IrcOutputHandler
{

    private BufferedWriter output = null;

    private Socket socket;

    public IrcOutputHandler( MBassador<IrcEvent> bus )
    {
        bus.subscribe( this );
    }

    /**
     * Converts internal IrcMessage to a raw message and sends it.
     * 
     * @param messageToSend
     * @throws NullPointerException in particular when the socket has never been connected, but not exclusively.
     */
    @Handler
    public void handleMessageSend( MessageSend messageToSend )
    {
        IrcMessage msg = messageToSend.getMessage();
        String rawCommand = msg.toString();

        while ( !Thread.interrupted() )
        {
            try
            {
                Logger.trace( "Sending IRC message '" + rawCommand + "'" );
                output.write( rawCommand + "\n" );
                break;
            }
            catch ( NullPointerException | IOException e )
            {
                Logger.trace( "Waiting before retring sending IRC message '" + rawCommand + "'" );
                // The need to reconnect will be detected by input worker
                // Message will be sent on reconnect. Not sure if this is 100% desirable.
                try
                {
                    Thread.sleep( IoConfig.WAIT_TIME );
                }
                catch ( InterruptedException e1 )
                {
                }
            }
        }

        try
        {
            output.flush();
        }
        catch ( IOException e )
        {
            Logger.error( e, "Could not flush output stream" );
        }
    }

    @Handler( priority = Integer.MIN_VALUE )
    public void handleConnect( IrcConnect event )
    {
        socket = event.getSocket();

        Logger.trace( "Creating output stream" );
        try
        {
            OutputStream outputStream = socket.getOutputStream();
            output = new BufferedWriter( new OutputStreamWriter( outputStream, IoConfig.CHARSET ) );
            Logger.trace( "Output stream created" );
        }
        catch ( IOException e )
        {
            Logger.error( e, "Could not open output stream" );
        }
    }
}
