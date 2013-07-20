/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import ca.szc.keratin.core.event.message.MessageReceive;
import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceivePong
    extends MessageReceive
{
    public static final String COMMAND = "PONG";

    private final String[] params;

    public ReceivePong( OutputQueue replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        params = message.getParams();
    }

    public String[] getParams()
    {
        return params;
    }
}
