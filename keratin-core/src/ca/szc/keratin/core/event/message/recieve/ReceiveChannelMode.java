/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import ca.szc.keratin.core.net.io.OutputQueue;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveChannelMode
    extends ReceiveMode
{
    public static final String COMMAND = "MODE";

    public ReceiveChannelMode( OutputQueue replyQueue, IrcMessage message )
    {
        super( replyQueue, message );
    }
}
