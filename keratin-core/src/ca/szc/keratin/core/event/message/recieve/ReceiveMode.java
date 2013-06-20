package ca.szc.keratin.core.event.message.recieve;

import net.engio.mbassy.bus.MBassador;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.message.MessageRecieve;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveMode
    extends MessageRecieve
{
    public static final String COMMAND = "MODE";

    public ReceiveMode( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );
    }
    
    // public ReceiveMode( MBassador<IrcEvent> bus, String prefix, String name, String mode )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, name, mode ));
    // }
}
