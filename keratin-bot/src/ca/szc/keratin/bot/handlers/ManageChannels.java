/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot.handlers;

import java.util.Set;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.bot.KeratinBot;
import ca.szc.keratin.bot.KeratinBot.Channel;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.event.message.recieve.ReceiveKick;
import ca.szc.keratin.core.event.message.send.SendJoin;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;

/**
 * Manages channel join status
 */
public class ManageChannels
{
    private final Set<Channel> channels;

    private final KeratinBot bot;

    public ManageChannels( KeratinBot bot, Set<Channel> channels )
    {
        this.bot = bot;
        this.channels = channels;
    }

    /**
     * Sends a join message for each of the given channels soon after a connection is made.
     */
    @Handler( priority = Integer.MIN_VALUE + 2 )
    private void initialConnectionHandler( IrcConnect event )
    {
        MBassador<IrcEvent> bus = event.getBus();

        Logger.trace( "Sending initial channel join messages" );
        for ( Channel channel : channels )
        {
            try
            {
                if ( channel.getKey() == null )
                    bus.publish( new SendJoin( bus, channel.getName() ) );
                else
                    bus.publish( new SendJoin( bus, channel.getName(), channel.getKey() ) );
            }
            catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
            {
                Logger.error( e, "Could not send join message for channel '{0}'", channel );
            }
        }
    }

    /**
     * Rejoins channels when kicked
     */
    @Handler
    public void onKick( ReceiveKick event )
    {
        String target = event.getTarget();

        if ( target.equals( bot.getNick() ) )
        {
            try
            {
                Thread.sleep( 500 );
            }
            catch ( InterruptedException e )
            {
            }

            String channelName = event.getChannel();

            // Add channel will automatically look up the key, if there was one, since this channel has been added
            // previously.
            bot.addChannel( channelName );
        }
    }
}
