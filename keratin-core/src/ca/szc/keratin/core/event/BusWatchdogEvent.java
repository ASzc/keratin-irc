package ca.szc.keratin.core.event;

import ca.szc.keratin.core.net.io.OutputQueue;

public class BusWatchdogEvent
    extends IrcEvent
{
    public BusWatchdogEvent( OutputQueue replyQueue )
    {
        super( replyQueue );
    }
}
