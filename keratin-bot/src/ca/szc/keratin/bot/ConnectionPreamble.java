/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.connection.IrcConnect;
import ca.szc.keratin.core.event.message.send.SendNick;
import ca.szc.keratin.core.event.message.send.SendUser;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;

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
        MBassador<IrcEvent> bus = event.getBus();

        Logger.trace( "Sending connection nick/user preamble" );
        try
        {
            bus.publish( new SendNick( bus, nick ) );
            bus.publish( new SendUser( bus, user, "0", realName ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Couldn't send user info preamble." );
        }
    }
}
