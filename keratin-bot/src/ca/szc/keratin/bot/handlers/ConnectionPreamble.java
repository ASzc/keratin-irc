/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot.handlers;

import java.util.LinkedList;
import java.util.List;

import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.IrcMessage;
import ca.szc.keratin.core.net.message.SendMessage;

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
        OutputQueue replyQueue = event.getReplyQueue();

        Logger.trace( "Sending connection nick/user preamble" );
        try
        {
            List<IrcMessage> messageList = new LinkedList<IrcMessage>();

            // TODO use SendMessage's static methods
            messageList.add( SendMessage.nick( nick ) );
            messageList.add( SendMessage.user( user, "0", realName ) );

            for ( IrcMessage message : messageList )
                replyQueue.offer( message );
        }
        catch ( InvalidMessageParamException e )
        {
            Logger.error( e, "Couldn't enqueue nick/user info preamble." );
        }
    }
}
