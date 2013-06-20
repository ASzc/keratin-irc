package ca.szc.keratin.core.event.message.recieve;

import net.engio.mbassy.bus.MBassador;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.message.MessageRecieve;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceiveReply
    extends MessageRecieve
{
    // Reply is a special case, encompassing all messages with numeric command parts
    
    public ReceiveReply( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );
    }
}
