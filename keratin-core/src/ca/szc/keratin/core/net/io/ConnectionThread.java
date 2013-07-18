/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.net.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.net.SocketFactory;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.IrcMessageEvent;
import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.event.connection.IrcDisconnect;
import ca.szc.keratin.core.event.message.MessageSend;
import ca.szc.keratin.core.event.message.recieve.ReceiveChannelMode;
import ca.szc.keratin.core.event.message.recieve.ReceiveInvite;
import ca.szc.keratin.core.event.message.recieve.ReceiveJoin;
import ca.szc.keratin.core.event.message.recieve.ReceiveKick;
import ca.szc.keratin.core.event.message.recieve.ReceiveMode;
import ca.szc.keratin.core.event.message.recieve.ReceiveNick;
import ca.szc.keratin.core.event.message.recieve.ReceiveNotice;
import ca.szc.keratin.core.event.message.recieve.ReceivePart;
import ca.szc.keratin.core.event.message.recieve.ReceivePing;
import ca.szc.keratin.core.event.message.recieve.ReceivePong;
import ca.szc.keratin.core.event.message.recieve.ReceivePrivmsg;
import ca.szc.keratin.core.event.message.recieve.ReceiveQuit;
import ca.szc.keratin.core.event.message.recieve.ReceiveReply;
import ca.szc.keratin.core.event.message.recieve.ReceiveTopic;
import ca.szc.keratin.core.event.message.recieve.ReceiveUserMode;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * Receives lines from the socket and sends out MessageRecieve events
 */
public class ConnectionThread
    extends Thread
{
    /**
     * States for the run loop
     */
    private enum RunState
    {
        CONNECT, READ, DISCONNECT, END
    }

    /**
     * How long a read on the socket will block before throwing SocketTimeoutException
     */
    private static final int SOCKET_TIMEOUT = 20 * 1000;

    private static boolean isDigits( String str )
    {
        try
        {
            Integer.parseInt( str );
            return true;
        }
        catch ( NumberFormatException e )
        {
            return false;
        }
    }

    private final MBassador<IrcEvent> bus;

    private final InetSocketAddress endpoint;

    private final SocketFactory socketFactory;

    private final Object outputMutex = new Object();

    private BufferedWriter output;

    public ConnectionThread( MBassador<IrcEvent> bus, InetSocketAddress endpoint, SocketFactory socketFactory )
    {
        this.bus = bus;
        this.endpoint = endpoint;
        this.socketFactory = socketFactory;

        bus.subscribe( this );
    }

    private void createOutput( Socket socket )
        throws IOException
    {
        Logger.trace( "Creating output" );
        OutputStream outputStream = socket.getOutputStream();
        synchronized ( outputMutex )
        {
            output = new BufferedWriter( new OutputStreamWriter( outputStream, IoConfig.CHARSET ) );
        }
        Logger.trace( "Output created" );
    }

    /**
     * Converts internal IrcMessage to a raw message and sends it.
     * 
     * @param messageToSend
     */
    @Handler
    private void handleMessageSend( MessageSend messageToSend )
    {
        IrcMessage msg = messageToSend.getMessage();
        String rawCommand = msg.toString();

        if ( output != null )
        {
            try
            {
                Logger.trace( "Sending IRC message '" + rawCommand + "'" );
                writeLine( rawCommand );
            }
            catch ( IOException e )
            {
                Logger.trace( "Error sending IRC message '" + rawCommand + "'" );
            }
        }
        else
        {
            Logger.trace( "First connection has not been established, can't send IRC message '" + rawCommand + "'" );
        }
    }

    @Override
    public void run()
    {
        Thread.currentThread().setName( "InputThread" );
        Logger.trace( "Input thread running" );

        RunState state = RunState.CONNECT;

        Socket socket = null;
        BufferedReader input = null;

        while ( state != RunState.END )
        {
            if ( Thread.interrupted() )
            {
                Logger.trace( "Interrupted, exiting" );
                state = RunState.END;
            }

            if ( state == RunState.CONNECT )
            {
                try
                {
                    Logger.trace( "Creating/connecting socket" );
                    socket = socketFactory.createSocket( endpoint.getAddress(), endpoint.getPort() );
                    Logger.info( "Successfully connected socket" );

                    socket.setSoTimeout( SOCKET_TIMEOUT );
                    createOutput( socket );

                    InputStream inputStream = socket.getInputStream();
                    input = new BufferedReader( new InputStreamReader( inputStream, IoConfig.CHARSET ) );
                    Logger.trace( "Input stream created" );

                    bus.publishAsync( new IrcConnect( bus, socket ) );

                    state = RunState.READ;
                }
                catch ( IOException e )
                {
                    Logger.error( e, "Failed to connect socket, sleeping before retrying." );
                }

                try
                {
                    Thread.sleep( IoConfig.WAIT_TIME );
                }
                catch ( InterruptedException e )
                {
                    state = RunState.END;
                }
            }
            else if ( state == RunState.READ )
            {
                try
                {
                    String line = input.readLine();
                    if ( line != null )
                    {
                        // Logger.trace( "Got line " + line );
                        try
                        {
                            IrcMessage message = IrcMessage.parseMessage( line );
                            // String prefix = message.getPrefix();
                            String command = message.getCommand();
                            // String[] params = message.getParams();

                            IrcMessageEvent messageEvent = null;

                            try
                            {
                                // INVITE
                                if ( ReceiveInvite.COMMAND.equals( command ) )
                                    messageEvent = new ReceiveInvite( bus, message );

                                // JOIN
                                else if ( ReceiveJoin.COMMAND.equals( command ) )
                                    messageEvent = new ReceiveJoin( bus, message );

                                // KICK
                                else if ( ReceiveKick.COMMAND.equals( command ) )
                                    messageEvent = new ReceiveKick( bus, message );

                                // MODE
                                else if ( ReceiveMode.COMMAND.equals( command ) )
                                {
                                    if ( message.getParams().length == 2 )
                                        messageEvent = new ReceiveUserMode( bus, message );
                                    else
                                        messageEvent = new ReceiveChannelMode( bus, message );
                                }

                                // NICK
                                else if ( ReceiveNick.COMMAND.equals( command ) )
                                    messageEvent = new ReceiveNick( bus, message );

                                // NOTICE
                                else if ( ReceiveNotice.COMMAND.equals( command ) )
                                    messageEvent = new ReceiveNotice( bus, message );

                                // PART
                                else if ( ReceivePart.COMMAND.equals( command ) )
                                    messageEvent = new ReceivePart( bus, message );

                                // PING
                                else if ( ReceivePing.COMMAND.equals( command ) )
                                    messageEvent = new ReceivePing( bus, message );

                                // PONG
                                else if ( ReceivePong.COMMAND.equals( command ) )
                                    messageEvent = new ReceivePong( bus, message );

                                // PRIVMSG
                                else if ( ReceivePrivmsg.COMMAND.equals( command ) )
                                    messageEvent = new ReceivePrivmsg( bus, message );

                                // QUIT
                                else if ( ReceiveQuit.COMMAND.equals( command ) )
                                    messageEvent = new ReceiveQuit( bus, message );

                                // replies
                                else if ( isDigits( command ) )
                                    messageEvent = new ReceiveReply( bus, message );

                                // TOPIC
                                else if ( ReceiveTopic.COMMAND.equals( command ) )
                                    messageEvent = new ReceiveTopic( bus, message );

                                // others
                                else
                                    Logger.error( "Unknown message '" + message.toString().replace( "\n", "\\n" ) + "'" );
                            }
                            catch ( Exception e )
                            {
                                Logger.error( "Error when creating message event, type: "
                                    + messageEvent.getClass().getSimpleName() + ", content: " + messageEvent );
                            }

                            if ( messageEvent != null )
                            {
                                Logger.trace( "Publishing message event type: "
                                    + messageEvent.getClass().getSimpleName() + ", content: " + messageEvent );
                                // TODO publication timeout
                                bus.publishAsync( messageEvent );
                            }
                        }
                        catch ( IndexOutOfBoundsException | InvalidMessagePrefixException
                                        | InvalidMessageCommandException | InvalidMessageParamException e )
                        {
                            Logger.error( e, "Couldn't create IrcMessage instance out of parsed data from line " + line );
                        }
                        catch ( InvalidMessageException e )
                        {
                            Logger.error( e, "Couldn't parse line " + line );
                        }
                    }
                    else
                    {
                        // Definite connection loss
                        Logger.error( "Input end of stream" );
                        state = RunState.DISCONNECT;
                    }
                }
                catch ( SocketTimeoutException e )
                {
                    Logger.trace( e, "Read line timed out, checking if connection is active" );

                    // Sending some data is pretty much the only way to tell if the TCP connection is still alive. We
                    // don't really care about the reply, just that sending it doesn't cause a connection error.
                    try
                    {
                        writeLine( "PING client" );
                    }
                    catch ( IOException e1 )
                    {
                        state = RunState.DISCONNECT;
                    }
                }
                catch ( IOException e )
                {
                    Logger.error( e, "Could not read line" );
                    state = RunState.DISCONNECT;
                }
            }
            else if ( state == RunState.DISCONNECT )
            {
                if ( socket != null && !socket.isClosed() )
                {
                    try
                    {
                        socket.close();
                    }
                    catch ( IOException e )
                    {
                        Logger.trace( e, "Error when closing open socket" );
                    }
                }

                bus.publishAsync( new IrcDisconnect( bus, socket ) );

                Logger.info( "Disconnected, attempting reconnect." );

                state = RunState.CONNECT;
            }
        }
        Logger.trace( "Run loop ends, thread exiting" );
    }

    private void writeLine( String line )
        throws IOException
    {
        synchronized ( outputMutex )
        {
            output.write( line + "\n" );

            try
            {
                output.flush();
            }
            catch ( IOException e )
            {
                Logger.error( e, "Could not flush output stream" );
            }
        }
    }
}
