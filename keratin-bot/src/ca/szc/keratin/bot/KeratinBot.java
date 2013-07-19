/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import net.engio.mbassy.bus.MBassador;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.bot.DelegateConnection.ConnectionRunnable;
import ca.szc.keratin.bot.annotation.AssignedBot;
import ca.szc.keratin.bot.annotation.HandlerContainerDetector;
import ca.szc.keratin.bot.handlers.ConnectionPreamble;
import ca.szc.keratin.bot.handlers.ManageChannels;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.message.send.SendJoin;
import ca.szc.keratin.core.event.message.send.SendMode;
import ca.szc.keratin.core.event.message.send.SendNick;
import ca.szc.keratin.core.event.message.send.SendPart;
import ca.szc.keratin.core.event.message.send.SendPrivmsg;
import ca.szc.keratin.core.net.IrcConnection;
import ca.szc.keratin.core.net.IrcConnection.SslMode;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.util.InvalidPortException;

/**
 * A class for supporting IRC bots.
 */
public class KeratinBot
{
    private String user;

    private String nick;

    private String realName;

    private String serverAddress;

    private int serverPort;

    private SslMode sslMode;

    private Map<String, Channel> channels;

    private boolean connectionActive;

    private MBassador<IrcEvent> connectionBus;

    private IrcConnection connection;

    private DelegateConnection delegateConn;

    /**
     * Make a KeratinBot with no fields predefined. Must have fields set before calling connect().
     */
    public KeratinBot()
    {
        connectionActive = false;
    }

    /**
     * Make a KeratinBot with all fields needed for a connection predefined.
     * 
     * @param user IRC username
     * @param nick IRC nick. This is the unique ID of a client on IRC
     * @param realName IRC full/real name
     * @param serverAddress The address of the server to connect to when connect() is called.
     * @param serverPort The port on the server to connect to when connect() is called.
     * @param sslMode {@link SslMode} value
     * @param initialChannels The channels to connect to initially, can be empty, but not null.
     */
    public KeratinBot( String user, String nick, String realName, String serverAddress, int serverPort,
                       SslMode sslMode, Channel[] initialChannels )
    {
        this();
        setUser( user );
        setNick( nick );
        setRealName( realName );
        setServerAddress( serverAddress );
        setServerPort( serverPort );
        setSslMode( sslMode );
        for ( Channel channel : initialChannels )
        {
            addChannel( channel.getName(), channel.getKey() );
        }
    }

    /**
     * Perform the connection. All fields must be defined before calling this, if using the blank constructor.
     */
    public void connect()
    {
        IrcConnection conn = null;
        try
        {
            conn = new IrcConnection( serverAddress, serverPort, sslMode );
        }
        catch ( UnknownHostException | InvalidPortException e )
        {
            Logger.error( e, "Could not make IrcConnection" );
        }

        connectionBus = conn.getEventBus();

        connectionBus.subscribe( new ConnectionPreamble( user, nick, realName ) );
        connectionBus.subscribe( new ManageChannels( this, channels ) );

        for ( Class<?> handlerContainer : HandlerContainerDetector.getContainers() )
        {
            try
            {
                // Create an instance of the annotated class
                Object listener = handlerContainer.getConstructor().newInstance();

                // Set @AssignedBot annotated fields in the listener instance to a reference to this KeratinBot.
                for ( Field field : handlerContainer.getDeclaredFields() )
                {
                    if ( field.getType().equals( this.getClass() ) )
                    {
                        if ( field.getAnnotation( AssignedBot.class ) != null )
                        {
                            field.setAccessible( true );
                            field.set( listener, this );
                        }
                    }
                }

                // Subscribe the instance, with references injected to the message bus
                connectionBus.subscribe( listener );
            }
            catch ( InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e )
            {
                Logger.error( e, "Could not create instance of handler container '{0}'", handlerContainer );
            }
        }

        for ( Channel channel : channels.values() )
        {
            connectionBus.subscribe( channel );
        }

        conn.connect();

        delegateConn = new DelegateConnection( serverAddress, serverPort, sslMode, user, nick + "-del", realName );

        connection = conn;
        connectionActive = true;
    }

    /**
     * End the connection.
     */
    public void disconnect()
    {
        connectionActive = false;
        connection.disconnect();
        connection = null;
        connectionBus = null;
    }

    /**
     * Get the current nick ID of the bot on the server.
     * 
     * @return Nick string
     */
    public String getNick()
    {
        return nick;
    }

    /**
     * Get the full/real name of the bot on the server.
     * 
     * @return Real name string, can contain spaces.
     */
    public String getRealName()
    {
        return realName;
    }

    /**
     * Get the address of the server the bot is connected to.
     * 
     * @return Server address, can be a domain name or IP address.
     */
    public String getServerAddress()
    {
        return serverAddress;
    }

    /**
     * Get the port of the server the bot is connected to.
     * 
     * @return Port number
     */
    public int getServerPort()
    {
        return serverPort;
    }

    /**
     * Get the user name of the bot on the server.
     * 
     * @return User string
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Get the SSL socket use status/mode.
     * 
     * @return {@link SslMode} value
     */
    public SslMode getSslMode()
    {
        return sslMode;
    }

    /**
     * Set the stored nick value, and send an update message to the server if connected.
     * 
     * @param nick A spaceless string, must start with an alphabetic character
     */
    public void setNick( String nick )
    {
        if ( connectionActive )
        {
            try
            {
                connectionBus.publishAsync( new SendNick( connectionBus, nick ) );
            }
            catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
            {
                Logger.error( e, "Could not send nick message for nick '{0}'", nick );
            }
        }
        this.nick = nick;
    }

    /**
     * Set the full/real name of the bot on the server. No effect after connect() has been called.
     * 
     * @param realName Real name string, can contain spaces.
     */
    public void setRealName( String realName )
    {
        this.realName = realName;
    }

    /**
     * Set the address of the server the bot going to connect to. No effect after connect() has been called.
     * 
     * @param serverAddress Server address, can be a domain name or IP address.
     */
    public void setServerAddress( String serverAddress )
    {
        this.serverAddress = serverAddress;
    }

    /**
     * Set the port of the server the bot going to connect to. No effect after connect() has been called.
     * 
     * @param serverAddress Server port, has to be a valid port number.
     */
    public void setServerPort( int serverPort )
    {
        this.serverPort = serverPort;
    }

    /**
     * Set if and how SSL sockets are going to be used. No effect after connect() has been called.
     * 
     * @param sslMode {@link SslMode} value
     */
    public void setSslMode( SslMode sslMode )
    {
        this.sslMode = sslMode;
    }

    /**
     * Set the user name the bot will use on the server. No effect after connect() has been called.
     * 
     * @param user User string
     */
    public void setUser( String user )
    {
        this.user = user;
    }

    /**
     * Get the set of current channels the bot is joined to.
     * 
     * @return Set of channel strings
     */
    public Collection<Channel> getChannels()
    {
        return channels.values();
    }

    /**
     * Get the Channel stored under a channel name.
     * 
     * @param name The name of the channel to get. Includes the # prefix.
     * @return The corresponding Channel, if it exists, else null
     */
    public Channel getChannel( String name )
    {
        return channels.get( name );
    }

    /**
     * Add a channel to the list of current channels the bot is joined to. Send an update message to the server
     * immediately if connected. If a channel was added previously with a key, the key will be looked up automatically
     * when being re-added.
     * 
     * @param name Channel's name
     */
    public void addChannel( String name )
    {
        addChannel( name, null );
    }

    /**
     * Add a channel to the list of current channels the bot is joined to. Send an update message to the server
     * immediately if connected. If a channel was added previously with a key, and the key given is null, the key will
     * be looked up automatically when being re-added.
     * 
     * @param name Channel's name
     * @param key Channel's key if it exists, otherwise null
     */
    public void addChannel( String name, String key )
    {
        if ( channels == null )
        {
            channels = new HashMap<String, Channel>();
        }

        if ( key == null )
        {
            for ( Entry<String, Channel> channelEntry : channels.entrySet() )
            {
                String channelName = channelEntry.getKey();
                Channel channel = channelEntry.getValue();
                String channelKey = channel.getKey();

                if ( channelKey != null && channelName.equals( name ) )
                {
                    key = channelEntry.getKey();
                    break;
                }
            }
        }

        addChannel( new Channel( name, key ) );
    }

    /**
     * Add a channel to the list of current channels the bot is joined to. Send an update message to the server if
     * connected.
     * 
     * @param channel The channel to add
     */
    public void addChannel( Channel channel )
    {
        if ( channels == null )
        {
            channels = new HashMap<String, Channel>();
        }

        if ( connectionActive )
        {
            try
            {
                if ( channel.getKey() == null )
                    connectionBus.publishAsync( new SendJoin( connectionBus, channel.getName() ) );
                else
                    connectionBus.publishAsync( new SendJoin( connectionBus, channel.getName(), channel.getKey() ) );
            }
            catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
            {
                Logger.error( e, "Could not send join message for channel '{0}'", channel );
            }

            connectionBus.subscribe( channel );
        }

        this.channels.put( channel.getName(), channel );
    }

    /**
     * Remove a channel from the list of current channels the bot is joined to. Send an update message to the server if
     * connected.
     * 
     * @param channel Channel to part from
     */
    public void remChannel( Channel channel )
    {
        remChannel( channel.getName() );
    }

    /**
     * Remove a channel from the list of current channels the bot is joined to. Send an update message to the server if
     * connected.
     * 
     * @param name Name of the channel to part from
     */
    public void remChannel( String name )
    {
        if ( connectionActive )
        {
            try
            {
                connectionBus.publishAsync( new SendPart( connectionBus, name ) );
            }
            catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
            {
                Logger.error( e, "Could not send part message for channel '{0}'", name );
            }
        }
        channels.remove( name );
    }

    /**
     * Op a single nick in a channel. Probably will fail if the bot is not an operator in the given channel.
     * 
     * @param channelName The channel to op the nick in
     * @param nick The nick to op in the channel
     */
    public void opNick( String channelName, String nick )
    {
        try
        {
            connectionBus.publishAsync( new SendMode( connectionBus, channelName, "+o", nick ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Couldn't send op mode change for nick '{0}' in channel '{1}'", nick, channelName );
        }
    }

    /**
     * Op one or more nicks in a single channel. Probably will fail if the bot is not an operator in the given channel.
     * 
     * @param channelName The channel to op the nicks in
     * @param nicks The nicks to op in the channel
     */
    public void opNicks( String channelName, Collection<String> nicks )
    {
        final int bufferSize = 4;

        // Op in groups of size bufferSize
        Iterator<String> iterator = nicks.iterator();
        while ( iterator.hasNext() )
        {
            List<String> nickBuffer = new LinkedList<String>();
            StringBuilder modeBuffer = new StringBuilder();
            modeBuffer.append( "+" );

            for ( int i = 0; iterator.hasNext() && i < bufferSize; )
            {
                String nick = iterator.next();
                nickBuffer.add( nick );
                modeBuffer.append( "o" );

                i++;
            }

            // Hijack nickBuffer so all the parameters can be given together in one array, satifying varargs
            nickBuffer.add( 0, modeBuffer.toString() );
            nickBuffer.add( 0, channelName );

            String[] paramArray = new String[nickBuffer.size()];
            nickBuffer.toArray( paramArray );
            if ( paramArray.length > 0 )
            {
                try
                {
                    connectionBus.publishAsync( new SendMode( connectionBus, paramArray ) );
                }
                catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
                {
                    Logger.error( e, "Couldn't send op mode change for nicks '{0}' in channel '{1}'", nickBuffer,
                                  channelName );
                }
            }
        }
    }

    /**
     * Use a seperate connection to send a PRIVMSG as a different nick to a certain channel.
     * 
     * @param nick The nick to use
     * @param channel The name of the channel. The bot must be in that channel.
     * @param text The text to send. Send multiple PRIVMSGs by including \n characters.
     */
    public void sendPrivmsgAs( String nick, String channel, final String text )
    {
        sendPrivmsgAs( nick, getChannel( channel ), text );
    }

    /**
     * Use a seperate connection to send a PRIVMSG as a different nick to a certain channel.
     * 
     * @param nick The nick to use
     * @param channel The channel to use
     * @param text The text to send. Send multiple PRIVMSGs by including \n characters.
     */
    public void sendPrivmsgAs( final String nick, final Channel channel, final String text )
    {
        try
        {
            delegateConn.offer( new ConnectionRunnable()
            {
                @Override
                public void run( IrcConnection conn )
                {
                    MBassador<IrcEvent> bus = conn.getEventBus();

                    try
                    {
                        bus.publish( new SendNick( bus, nick ) );
                        bus.publish( new SendJoin( bus, channel.getName() ) );
                        try
                        {
                            // Despite the messages being sent in order on our end, sometimes the server doesn't catch
                            // up to the channel join in time. Wait a short arbitrary period to make this less likely.
                            Thread.sleep( 50 );
                        }
                        catch ( InterruptedException e )
                        {
                        }
                        bus.publishAsync( new SendPrivmsg( bus, channel.getName(), text ) );
                        bus.publishAsync( new SendPart( bus, channel.getName() ) );
                    }
                    catch ( InvalidMessagePrefixException | InvalidMessageCommandException
                                    | InvalidMessageParamException e )
                    {
                        Logger.error( e, "Could not send an IRC message" );
                    }
                }
            }, 10, TimeUnit.SECONDS );
        }
        catch ( InterruptedException e )
        {
            Logger.error( e, "Could not enqueue delagate task" );
        }
    }
}
