package ca.szc.keratin.core.event.connection;

import java.net.Socket;

import net.engio.mbassy.bus.MBassador;

import ca.szc.keratin.core.event.IrcConnectionEvent;
import ca.szc.keratin.core.event.IrcEvent;

/**
 * Socket has connected
 */
public class IrcConnect
    extends IrcConnectionEvent
{

    public IrcConnect( MBassador<IrcEvent> bus, Socket socket )
    {
        super( bus, socket );
    }

}
