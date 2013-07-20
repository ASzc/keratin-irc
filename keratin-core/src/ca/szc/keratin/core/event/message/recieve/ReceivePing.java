/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.message.MessageReceive;
import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceivePing
    extends MessageReceive
{
    public static final String COMMAND = "PING";

    private final String[] params;

    public ReceivePing( OutputQueue replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        params = message.getParams();
    }

    public String[] getParams()
    {
        return params;
    }

    public void pong()
    {
        try
        {
            if ( params.length == 1 )
                getReplyQueue().pong( params[0] );
            else if ( params.length == 2 )
                getReplyQueue().pong( params[0], params[1] );
            else
                Logger.error( "Can't PONG, invalid number of PING message parameters" );
        }
        catch ( InvalidMessageParamException e )
        {
            Logger.error( e, "Error creating reply message" );
        }
    }
}
