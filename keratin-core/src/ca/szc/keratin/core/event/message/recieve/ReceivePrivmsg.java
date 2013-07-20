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
import ca.szc.keratin.core.misc.LineWrap;
import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceivePrivmsg
    extends MessageReceive
    implements Replyable, DirectlyReplyable, PrivatelyReplyable
{
    public static final String COMMAND = "PRIVMSG";

    private final String channel;

    private final String sender;

    private final String text;

    public ReceivePrivmsg( OutputQueue replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        sender = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        if ( !message.getParams()[0].startsWith( "#" ) )
            channel = sender;
        else
            channel = message.getParams()[0];
        text = message.getParams()[1].substring( 1 );
    }

    public String getChannel()
    {
        return channel;
    }

    public String getSender()
    {
        return sender;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public void reply( String reply )
    {
        try
        {
            for ( String line : LineWrap.wrap( reply ) )
                getReplyQueue().privmsg( channel, line );
        }
        catch ( InvalidMessageParamException e )
        {
            Logger.error( e, "Error creating reply message" );
        }
    }

    @Override
    public void replyDirectly( String reply )
    {
        reply( sender + ": " + reply );
    }

    @Override
    public void replyPrivately( String reply )
    {
        try
        {
            for ( String line : LineWrap.wrap( reply ) )
                getReplyQueue().privmsg( sender, line );
        }
        catch ( InvalidMessageParamException e )
        {
            Logger.error( e, "Error creating reply message" );
        }
    }
}
