package ca.szc.keratin.core.event;

import net.engio.mbassy.bus.MBassador;

/**
 * An event tied to some part of IRC
 */
public abstract class IrcEvent
{

    private final MBassador<IrcEvent> bus;

    /**
     * Reference to the bus that carried this event, so that reply messages are easily possible.
     * 
     * @return The bus that carried this event
     */
    public MBassador<IrcEvent> getBus()
    {
        return bus;
    }

    public IrcEvent( MBassador<IrcEvent> bus )
    {
        this.bus = bus;
    }

}
