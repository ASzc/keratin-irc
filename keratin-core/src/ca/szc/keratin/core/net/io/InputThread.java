package ca.szc.keratin.core.net.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.event.connection.IrcDisconnect;
import ca.szc.keratin.core.event.message.recieve.ReceiveInvite;
import ca.szc.keratin.core.event.message.recieve.ReceiveJoin;
import ca.szc.keratin.core.event.message.recieve.ReceiveKick;
import ca.szc.keratin.core.event.message.recieve.ReceiveNotice;
import ca.szc.keratin.core.event.message.recieve.ReceivePart;
import ca.szc.keratin.core.event.message.recieve.ReceivePing;
import ca.szc.keratin.core.event.message.recieve.ReceivePrivmsg;
import ca.szc.keratin.core.event.message.recieve.ReceiveQuit;
import ca.szc.keratin.core.event.message.recieve.ReceiveReply;
import ca.szc.keratin.core.event.message.recieve.ReceiveTopic;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * Receives lines from the socket and sends out MessageRecieve events
 */
public class InputThread
    extends Thread
{

    private final MBassador<IrcEvent> bus;

    private volatile boolean connected;

    private BufferedReader input = null;

    private Socket socket;

    public InputThread( MBassador<IrcEvent> bus )
    {
        this.bus = bus;
        connected = false;
    }

    @Override
    public void run()
    {
        Logger.trace( "Input thread running" );
        bus.subscribe( this );

        while ( !Thread.interrupted() )
        {
            if ( connected )
            {
                // Logger.trace( "Reading line from input" );
                try
                {
                    String line = input.readLine();
                    if ( line != null )
                    {
                        Logger.trace( "Got line " + line );
                        IrcMessage message = null;
                        try
                        {
                            message = IrcMessage.parseMessage( line );
                            // String prefix = message.getPrefix();
                            String command = message.getCommand();
                            // String[] params = message.getParams();

                            if ( ReceiveInvite.COMMAND.equals( command ) )
                                bus.publishAsync( new ReceiveInvite( bus, message ) );
                            else if ( ReceiveJoin.COMMAND.equals( command ) )
                                bus.publishAsync( new ReceiveJoin( bus, message ) );
                            else if ( ReceiveKick.COMMAND.equals( command ) )
                                bus.publishAsync( new ReceiveKick( bus, message ) );
                            else if ( ReceiveNotice.COMMAND.equals( command ) )
                                bus.publishAsync( new ReceiveNotice( bus, message ) );
                            else if ( ReceivePart.COMMAND.equals( command ) )
                                bus.publishAsync( new ReceivePart( bus, message ) );
                            else if ( ReceivePing.COMMAND.equals( command ) )
                                bus.publishAsync( new ReceivePing( bus, message ) );
                            else if ( ReceivePrivmsg.COMMAND.equals( command ) )
                                bus.publishAsync( new ReceivePrivmsg( bus, message ) );
                            else if ( ReceiveQuit.COMMAND.equals( command ) )
                                bus.publishAsync( new ReceiveQuit( bus, message ) );
                            else if ( ReceiveTopic.COMMAND.equals( command ) )
                                bus.publishAsync( new ReceiveTopic( bus, message ) );
                            else if ( isDigits( command ) )
                                bus.publishAsync( new ReceiveReply( bus, message ) );
                            else
                                Logger.error( "Unknown message '" + message.toString().replace( "\n", "\\n" ) + "'" );

                            Logger.trace( "Done sending parsed message to bus" );
                        }
                        catch ( IndexOutOfBoundsException | InvalidMessagePrefixException
                                        | InvalidMessageCommandException | InvalidMessageParamException e )
                        {
                            Logger.error( e, "Couldn't publish parsed message '{0}' from line '{1}'", message, line );
                        }
                        catch ( InvalidMessageException e )
                        {
                            Logger.error( e, "Couldn't parse line '{0}'", line );
                        }

                        // Logger.trace( "Line processed from input" );
                    }
                    else
                    {
                        Logger.error( "Input end of stream" );
                        connected = false;
                        bus.publishAsync( new IrcDisconnect( bus, socket ) );
                    }
                }
                catch ( IOException e )
                {
                    Logger.error( e, "Could not read line" );
                    try
                    {
                        Thread.sleep( IoConfig.WAIT_TIME );
                    }
                    catch ( InterruptedException e1 )
                    {
                    }
                }
            }
            else
            {
                Logger.trace( "Socket is not connected. Sleeping." );
                try
                {
                    Thread.sleep( IoConfig.WAIT_TIME );
                }
                catch ( InterruptedException e )
                {
                }
            }
        }
        Logger.trace( "Interrupted, exiting" );
    }

    private boolean isDigits( String str )
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

    @Handler( priority = Integer.MIN_VALUE )
    public void handleConnect( IrcConnect event )
    {
        socket = event.getSocket();

        Logger.trace( "Creating input stream" );
        try
        {
            InputStream inputStream = socket.getInputStream();
            input = new BufferedReader( new InputStreamReader( inputStream, IoConfig.CHARSET ) );
            Logger.trace( "Input stream created" );
        }
        catch ( IOException e )
        {
            Logger.error( e, "Could not open input stream" );
        }

        connected = true;
    }
}
