/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot.handlers;

import java.util.Map;
import java.util.Map.Entry;

import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.bot.Channel;
import ca.szc.keratin.bot.KeratinBot;
import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.event.message.recieve.ReceiveKick;
import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;

/**
 * Manages channel join status
 */
public class ManageChannels
{
    private final Map<String, Channel> channels;

    private final KeratinBot bot;

    public ManageChannels( KeratinBot bot, Map<String, Channel> channels )
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
        Logger.trace( "Sending initial channel join messages" );
        for ( Entry<String, Channel> channelEntry : channels.entrySet() )
        {
            OutputQueue replyQueue = event.getReplyQueue();
            Channel channel = channelEntry.getValue();
            try
            {
                if ( channel.getKey() == null )
                    replyQueue.join( channel.getName() );
                else
                    replyQueue.join( channel.getName(), channel.getKey() );
            }
            catch ( InvalidMessageParamException e )
            {
                Logger.error( e, "Error creating IRC message" );
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
