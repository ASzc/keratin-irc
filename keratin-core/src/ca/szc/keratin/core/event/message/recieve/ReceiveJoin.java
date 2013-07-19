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
import ca.szc.keratin.core.event.message.send.SendPrivmsg;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveJoin
    extends MessageRecieve
    implements Replyable, DirectlyReplyable, PrivatelyReplyable
{
    public static final String COMMAND = "JOIN";

    private final String channel;

    private final String joiner;

    public ReceiveJoin( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );

        joiner = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        channel = message.getParams()[0].substring( 1 );
    }

    // public ReceiveJoin( MBassador<IrcEvent> bus, String prefix, String channel )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, channel ) );
    // }

    public String getJoiner()
    {
        return joiner;
    }

    public String getChannel()
    {
        return channel;
    }

    @Override
    public void replyPrivately( String reply )
    {
        try
        {
            getBus().publishAsync( new SendPrivmsg( getBus(), joiner, reply ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Error sending reply" );
        }
    }

    @Override
    public void replyDirectly( String reply )
    {
        reply( joiner + ": " + reply );
    }

    @Override
    public void reply( String reply )
    {
        try
        {
            getBus().publishAsync( new SendPrivmsg( getBus(), channel, reply ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Error sending reply" );
        }
    }
}
