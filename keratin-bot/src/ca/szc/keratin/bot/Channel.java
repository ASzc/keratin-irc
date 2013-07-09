package ca.szc.keratin.bot;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.pmw.tinylog.Logger;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import ca.szc.keratin.bot.util.CachedValue;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.event.message.recieve.ReceiveReply;
import ca.szc.keratin.core.event.message.send.SendNames;
import ca.szc.keratin.core.net.message.InvalidMessageCommandException;
import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.InvalidMessagePrefixException;
import ca.szc.keratin.core.net.message.IrcMessage;

/**
 * Holds data for one channel
 */
public class Channel
{
    private final String name;

    private final String key;

    private CachedValue<List<String>> nicks;

    /**
     * @param channelName The channel's name. Including the #. Cannot be null.
     * @param channelKey The channel's key. May be null if there is no key.
     */
    public Channel( String channelName, String channelKey )
    {
        this.name = channelName;
        this.key = channelKey;
    }

    public void setBus( final MBassador<IrcEvent> bus )
    {
        nicks = new CachedValue<List<String>>()
        {
            Semaphore replyAvailable = new Semaphore( 0 );

            private List<String> nicks;

            @Override
            protected List<String> updateValue()
            {
                bus.subscribe( this );
                try
                {
                    bus.publishAsync( new SendNames( bus, name ) );
                    try
                    {
                        replyAvailable.acquire();
                        return nicks;
                    }
                    catch ( InterruptedException e )
                    {
                        Logger.error( "Interrupted while waiting for NAMES reply", e );
                    }
                }
                catch ( InvalidMessagePrefixException | InvalidMessageCommandException | InvalidMessageParamException e )
                {
                    Logger.error( "Error sending NAMES message", e );
                }
                finally
                {
                    bus.unsubscribe( this );
                }
                return null;
            }

            @Handler
            private void namesReply( ReceiveReply event )
            {
                IrcMessage msg = event.getMessage();
                String[] params = msg.getParams();
                String replyNum = msg.getCommand();
                if ( "353".equals( replyNum ) && name.equals( params[2] ) )
                {
                    String nicksBlob = params[3];
                    nicks = Arrays.asList( nicksBlob.split( " " ) );

                    String first = nicks.get( 0 );
                    if ( first.startsWith( ":" ) )
                        nicks.set( 0, first.substring( 1 ) );

                    replyAvailable.release();
                }
            }

            @Override
            protected long expiryMillis()
            {
                return 5000;
            }
        };
    }

    /**
     * @return The channel's name. Including the #. Cannot be null.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The channel's key. May be null if there is no key.
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Get the nicks in the channel. Nicks that are special (like operators) will have a special character prepended to
     * them. Requires an active connection.
     * 
     * @return The NAMES list of this channel, or null on error.
     */
    public List<String> getNicks()
    {
        return nicks.getValue();
    }

    /**
     * Get the nicks of all regular users in the channel. Requires an active connection.
     * 
     * @return The filtered NAMES list of this channel, or null on error.
     */
    public List<String> getRegularNicks()
    {
        LinkedList<String> filteredList = new LinkedList<String>();

        for ( String nick : nicks.getValue() )
        {
            if ( !nick.startsWith( "@" ) )
                filteredList.add( nick );
        }

        return filteredList;
    }

    /**
     * Get the nicks of all operator users in the channel. Requires an active connection.
     * 
     * @return The filtered NAMES list of this channel, or null on error.
     */
    public List<String> getOperatorNicks()
    {
        LinkedList<String> filteredList = new LinkedList<String>();

        for ( String nick : nicks.getValue() )
        {
            if ( nick.startsWith( "@" ) )
                filteredList.add( nick.substring( 1 ) );
        }

        return filteredList;
    }

    /**
     * @return true iff the nick is in the channel and is an op in the channel
     */
    public boolean isOp( String nick )
    {
        return nicks.getValue().contains( "@" + nick );
    }

    @Override
    public String toString()
    {
        return "Channel [name=" + name + ", key=" + key + "]";
    }
}