package ca.szc.keratin.core.event.message;

import net.engio.mbassy.bus.MBassador;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.IrcMessageEvent;
import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * IRC messages being received
 */
public abstract class MessageRecieve
    extends IrcMessageEvent
{

    public MessageRecieve( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );
    }

}