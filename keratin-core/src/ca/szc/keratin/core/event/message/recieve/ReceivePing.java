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
import ca.szc.keratin.core.event.message.send.SendPong;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceivePing
    extends MessageRecieve
{
    public static final String COMMAND = "PING";

    private final String[] params;

    public ReceivePing( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );

        params = message.getParams();
    }

    // public ReceivePing( MBassador<IrcEvent> bus, String prefix, String server1, String server2 )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, server1, server2 ));
    // }

    public String[] getParams()
    {
        return params;
    }

    public void pong() {
        try
        {
            getBus().publishAsync( new SendPong( getBus(), getParams() ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Couldn't reply to ping." );
        }
    }
}
