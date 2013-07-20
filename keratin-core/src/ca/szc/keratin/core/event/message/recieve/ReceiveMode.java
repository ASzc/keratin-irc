/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import java.util.Arrays;
import java.util.List;

import ca.szc.keratin.core.event.message.MessageReceive;
import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveMode
    extends MessageReceive
{
    public static final String COMMAND = "MODE";

    // private final String sender;

    private final List<String> flagParams;

    private final String flags;

    private final String target;

    public ReceiveMode( OutputQueue replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        String[] params = message.getParams();

        // sender = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        target = params[0];
        flags = params[1];

        // Channel mode or user mode?
        if ( params.length > 2 )
        {
            // Channel mode
            flagParams = Arrays.asList( Arrays.copyOfRange( params, 2, params.length ) );
            String firstFlagParam = flagParams.get( 0 );
            if ( firstFlagParam.startsWith( ":" ) )
                flagParams.set( 0, firstFlagParam.substring( 1 ) );
        }
        else
        {
            // User mode
            flagParams = null;
        }
    }

    // public String getSender()
    // {
    // return sender;
    // }

    public List<String> getFlagParams()
    {
        return flagParams;
    }

    public String getFlags()
    {
        return flags;
    }

    public String getTarget()
    {
        return target;
    }
}
