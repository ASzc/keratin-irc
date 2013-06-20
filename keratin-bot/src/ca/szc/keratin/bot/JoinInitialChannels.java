/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot;

import java.util.List;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.event.message.send.SendJoin;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;

public class JoinInitialChannels
{
    private List<String> channels;

    public JoinInitialChannels( List<String> channels )
    {
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
        for ( String channel : channels )
        {
            try
            {
                bus.publish( new SendJoin( bus, channel ) );
            }
            catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
            {
                Logger.error( e, "Could not send join message for channel '{0}'", channel );
            }
        }
    }
}
