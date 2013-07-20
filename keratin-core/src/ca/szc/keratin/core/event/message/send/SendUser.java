/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.event.message.send;

import java.util.concurrent.BlockingQueue;

import ca.szc.keratin.core.event.message.MessageSend;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class SendUser
    extends MessageSend
{
    public static final String COMMAND = "USER";

    public SendUser( BlockingQueue<IrcMessage> replyQueue, String user, String mode, String realname )
        throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    {
        // TODO validation
        // * is used in the original "hostname" parameter that is now unused
        super( replyQueue, new IrcMessage( null, COMMAND, user, mode, "*", realname ) );
    }
}
