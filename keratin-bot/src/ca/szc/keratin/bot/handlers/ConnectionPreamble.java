/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot.handlers;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ConnectionPreamble
{
    private String user;

    private String nick;

    private String realName;

    public ConnectionPreamble( String user, String nick, String realName )
    {
        this.user = user;
        this.nick = nick;
        this.realName = realName;
    }

    /**
     * Sends the two required NICK and USER messages in sequence immediately after a connection is made.
     */
    @Handler( priority = Integer.MIN_VALUE + 1 )
    private void initialConnectionHandler( IrcConnect event )
    {
        BlockingQueue<IrcMessage> replyQueue = event.getReplyQueue();

        Logger.trace( "Sending connection nick/user preamble" );
        try
        {
            List<IrcMessage> messageList = new LinkedList<IrcMessage>();

            messageList.add( new IrcMessage( null, "NICK", nick ) );
            messageList.add( new IrcMessage( null, "USER", user, "0", "*", realName ) );

            for ( IrcMessage message : messageList )
                replyQueue.offer( message );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Couldn't enqueue user info preamble." );
        }
    }
}
