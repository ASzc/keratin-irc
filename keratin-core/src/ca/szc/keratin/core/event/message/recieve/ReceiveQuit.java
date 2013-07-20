/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.recieve;

import java.util.concurrent.BlockingQueue;

import ca.szc.keratin.core.event.message.MessageReceive;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveQuit
    extends MessageReceive
{
    public static final String COMMAND = "QUIT";

    private final String quitter;

    private final String text;

    public ReceiveQuit( BlockingQueue<IrcMessage> replyQueue, IrcMessage message )
    {
        super( replyQueue, message );

        quitter = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        text = message.getParams()[0].substring( 1 );
    }

    public String getQuitter()
    {
        return quitter;
    }

    public String getText()
    {
        return text;
    }
}
