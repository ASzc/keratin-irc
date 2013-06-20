package ca.szc.keratin.core.event.message.recieve;

import net.engio.mbassy.bus.MBassador;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.message.MessageRecieve;
import ca.szc.keratin.core.event.message.interfaces.DirectlyReplyable;
import ca.szc.keratin.core.event.message.interfaces.PrivatelyReplyable;
import ca.szc.keratin.core.event.message.interfaces.Replyable;
import ca.szc.keratin.core.event.message.send.SendPrivmsg;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

public class ReceivePrivmsg
    extends MessageRecieve
    implements Replyable, DirectlyReplyable, PrivatelyReplyable
{
    public static final String COMMAND = "PRIVMSG";

    private final String channel;

    private final String sender;

    private final String text;

    public ReceivePrivmsg( MBassador<IrcEvent> bus, IrcMessage message )
    {
        super( bus, message );

        sender = message.getPrefix().substring( 0, message.getPrefix().indexOf( '!' ) );
        channel = message.getParams()[0];
        text = message.getParams()[1].substring( 1 );
    }

    // public ReceivePrivmsg( MBassador<IrcEvent> bus, String prefix, String nick, String text )
    // throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    // {
    // super( bus, new IrcMessage( prefix, COMMAND, nick, text ) );
    //
    // this.sender = null;
    // this.channel = null;
    // this.text = null;
    // }

    public String getSender()
    {
        return sender;
    }

    public String getChannel()
    {
        return channel;
    }

    public String getText()
    {
        return text;
    }

    public void reply( String reply )
    {
        try
        {
            getBus().publish( new SendPrivmsg( getBus(), channel, reply ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Error sending reply" );
        }
    }

    public void replyDirectly( String reply )
    {
        reply( sender + ": " + reply );
    }

    public void replyPrivately( String reply )
    {
        try
        {
            getBus().publish( new SendPrivmsg( getBus(), sender, reply ) );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
        {
            Logger.error( e, "Error sending reply" );
        }
    }
}
