package ca.szc.keratin.core.net.io;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import ca.szc.keratin.core.net.message.InvalidMessageParamException;
import ca.szc.keratin.core.net.message.IrcMessage;
import ca.szc.keratin.core.net.message.SendMessage;

/**
 * Wraps a BlockingQueue that allows sending output booleans. Allows limited access to that BlockingQueue.
 */
public class OutputQueue
{
    private final BlockingQueue<IrcMessage> delegate;

    public OutputQueue( BlockingQueue<IrcMessage> delegate )
    {
        this.delegate = delegate;
    }

    /**
     * @see BlockingQueue#offer(Object)
     */
    public boolean offer( IrcMessage message )
    {
        return delegate.offer( message );
    }

    /**
     * @see BlockingQueue#offer(Object, long, TimeUnit)
     */
    public boolean offer( IrcMessage message, long timeout, TimeUnit unit )
        throws InterruptedException
    {
        return delegate.offer( message, timeout, unit );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#invite(String, String)
     */
    public boolean invite( String nick, String channel )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.invite( nick, channel ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#join(String)
     */
    public boolean join( String channel )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.join( channel ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#join(String, String)
     */
    public boolean join( String channel, String key )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.join( channel, key ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#kick(String, String)
     */
    public boolean kick( String channel, String nick )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.kick( channel, nick ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#kick(String, String, String)
     */
    public boolean kick( String channel, String nick, String comment )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.kick( channel, nick, comment ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#mode(String, String, String...)
     */
    public boolean mode( String target, String mode, String... params )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.mode( target, mode, params ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#names(String)
     */
    public boolean names( String channel )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.names( channel ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#nick(String)
     */
    public boolean nick( String nick )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.nick( nick ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#notice(String, String)
     */
    public boolean notice( String target, String text )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.notice( target, text ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#part(String)
     */
    public boolean part( String channel )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.part( channel ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#ping(String)
     */
    public boolean ping( String server1 )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.ping( server1 ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#ping(String, String)
     */
    public boolean ping( String server1, String server2 )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.ping( server1, server2 ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#pong(String)
     */
    public boolean pong( String daemon1 )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.pong( daemon1 ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#pong(String, String)
     */
    public boolean pong( String daemon1, String daemon2 )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.pong( daemon1, daemon2 ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#privmsg(String, String)
     */
    public boolean privmsg( String target, String text )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.privmsg( target, text ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#quit()
     */
    public boolean quit()
        throws InvalidMessageParamException
    {
        return offer( SendMessage.quit() );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#quit(String)
     */
    public boolean quit( String comment )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.quit( comment ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#topic(String)
     */
    public boolean topic( String channel )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.topic( channel ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#topic(String, String)
     */
    public boolean topic( String channel, String topic )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.topic( channel, topic ) );
    }

    /**
     * Construct the named message then offer it.
     * 
     * @see SendMessage#user(String, String, String)
     */
    public boolean user( String user, String mode, String realName )
        throws InvalidMessageParamException
    {
        return offer( SendMessage.user( user, mode, realName ) );
    }
}
