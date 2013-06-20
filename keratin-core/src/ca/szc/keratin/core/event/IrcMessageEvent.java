package ca.szc.keratin.core.event;

import net.engio.mbassy.bus.MBassador;
import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * An IRC event about a protocol message
 */
public abstract class IrcMessageEvent
    extends IrcEvent
{

    private final IrcMessage message;

    public IrcMessageEvent( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus );
        this.message = message;
    }

    /**
     * Get the IRC message
     */
    public IrcMessage getMessage()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return message.toString();
    }

}
