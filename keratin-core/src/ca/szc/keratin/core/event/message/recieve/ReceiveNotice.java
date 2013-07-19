/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import org.pmw.tinylog.Logger;

import net.engio.mbassy.bus.MBassador;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.message.MessageRecieve;
import ca.szc.keratin.core.event.message.interfaces.DirectlyReplyable;
import ca.szc.keratin.core.event.message.interfaces.PrivatelyReplyable;
import ca.szc.keratin.core.event.message.interfaces.Replyable;
import ca.szc.keratin.core.event.message.send.SendNotice;
import ca.szc.keratin.core.misc.LineWrap;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveNotice
    extends MessageRecieve
    implements Replyable, DirectlyReplyable, PrivatelyReplyable
{
    public static final String COMMAND = "NOTICE";

    private final String channel;

    private final String sender;

    private final String text;

    public ReceiveNotice( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );

        if ( message.getPrefix() != null )
        {
            int splitPoint = message.getPrefix().indexOf( '!' );
            if ( splitPoint != -1 )
                sender = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
            else
                sender = message.getPrefix();
        }
        else
            sender = "";

        if ( !message.getParams()[0].startsWith( "#" ) )
            channel = sender;
        else
            channel = message.getParams()[0];

        text = message.getParams()[1].substring( 1 );
    }

    // public ReceiveNotice( MBassador<IrcEvent> bus, String prefix, String nick, String text )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, nick, text ) );
    // }

    public String getSender()
    {
        return sender;
    }

    public String getChannel()
    {
        return channel;
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
                getBus().publishAsync( new SendNotice( getBus(), channel, line ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Error sending reply" );
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
                getBus().publishAsync( new SendNotice( getBus(), sender, line ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Error sending reply" );
        }
    }
}
