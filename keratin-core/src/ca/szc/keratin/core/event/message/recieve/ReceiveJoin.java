/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.message.MessageReceive;
import ca.szc.keratin.core.event.message.interfaces.DirectlyReplyable;
import ca.szc.keratin.core.event.message.interfaces.PrivatelyReplyable;
import ca.szc.keratin.core.event.message.interfaces.Replyable;
import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveJoin
    extends MessageReceive
    implements Replyable, DirectlyReplyable, PrivatelyReplyable
{
    public static final String COMMAND = "JOIN";

    private final String channel;

    private final String joiner;

    public ReceiveJoin( OutputQueue replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        joiner = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        channel = message.getParams()[0].substring( 1 );
    }

    public String getChannel()
    {
        return channel;
    }

    public String getJoiner()
    {
        return joiner;
    }

    @Override
    public void reply( String reply )
    {
        try
        {
            getReplyQueue().privmsg( channel, reply );
        }
        catch ( InvalidMessageParamException e )
        {
            Logger.error( e, "Error creating reply message" );
        }
    }

    @Override
    public void replyDirectly( String reply )
    {
        reply( joiner + ": " + reply );
    }

    @Override
    public void replyPrivately( String reply )
    {
        try
        {
            getReplyQueue().privmsg( joiner, reply );
        }
        catch ( InvalidMessageParamException e )
        {
            Logger.error( e, "Error creating reply message" );
        }
    }
}
