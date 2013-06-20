package ca.szc.keratin.core.event.message.send;

import net.engio.mbassy.bus.MBassador;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.message.MessageSend;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class SendUser
    extends MessageSend
{
    public static final String COMMAND = "USER";

    public SendUser( MBassador<IrcEvent> bus, String user, String mode, String realname )
        throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    {
        // TODO validation
        // * is used in the original "hostname" parameter that is now unused
        super( bus, new IrcMessage( null, COMMAND, user, mode, "*", realname ));
    }
}
