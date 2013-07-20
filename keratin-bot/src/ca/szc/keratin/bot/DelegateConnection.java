package ca.szc.keratin.bot;

import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.engio.mbassy.bus.MBassador;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.bot.handlers.ConnectionPreamble;
import ca.szc.keratin.core.event.IrcEvent;
import ca.szc.keratin.core.net.IrcConnection;
import ca.szc.keratin.core.net.IrcConnection.SslMode;
import ca.szc.keratin.core.net.util.InvalidPortException;

/**
 * A connection to be delagated short-term tasks. This is in the public API, but users should probably use
 * {@link KeratinBot} instead.
 */
public class DelegateConnection
{
    /**
     * Basically a Runnable, but called with an IrcConnection instead of nothing.
     */
    public static interface ConnectionRunnable
    {
        public void run( IrcConnection conn );
    }

    private IrcConnection connection;

    private final BlockingQueue<DelegateConnection.ConnectionRunnable> taskQueue;

    private final String address;

    private final int port;

    private final String user;

    private final String nick;

    private final String realName;

    private final SslMode sslMode;

    /**
     * Create with given information. Connection will not be made until the first task is recieved.
     * 
     * @param address Server address
     * @param port Server port
     * @param sslMode
     * @param user User name
     * @param nick Initial nick
     * @param realName Real name
     */
    public DelegateConnection( String address, int port, SslMode sslMode, String user, String nick, String realName )
    {
        taskQueue = new LinkedBlockingQueue<DelegateConnection.ConnectionRunnable>();

        this.address = address;
        this.port = port;
        this.sslMode = sslMode;
        this.user = user;
        this.nick = nick;
        this.realName = realName;

        new Thread()
        {
            @Override
            public void run()
            {
                Thread.currentThread().setName( "DelegateConnection" );
                while ( !Thread.interrupted() )
                {
                    try
                    {
                        ConnectionRunnable task = taskQueue.take();
                        Logger.trace( "Got delegate connection task" );
                        task.run( getConnection() );
                    }
                    catch ( InterruptedException e )
                    {
                        break;
                    }
                }
                Logger.trace( "Run loop ends, exiting" );

                if ( connection != null )
                {
                    Logger.trace( "Disconnecting delegate connection" );
                    connection.disconnect();
                }
            }
        }.start();
    }

    private IrcConnection getConnection()
    {
        if ( connection == null )
        {
            Logger.info( "Starting delegate connection" );
            try
            {
                IrcConnection conn = new IrcConnection( address, port, sslMode );

                MBassador<IrcEvent> bus = conn.getEventBus();
                bus.subscribe( new ConnectionPreamble( user, nick, realName ) );

                conn.connect();
                connection = conn;
            }
            catch ( UnknownHostException | InvalidPortException e )
            {
                Logger.error( e, "Could not make IrcConnection" );
            }
        }
        return connection;
    }

    /**
     * @see {@link LinkedBlockingQueue#offer(Object)}
     */
    public boolean offer( DelegateConnection.ConnectionRunnable task )
    {
        return taskQueue.offer( task );
    }

    /**
     * @see {@link LinkedBlockingQueue#offer(Object, long, TimeUnit)}
     */
    public void offer( DelegateConnection.ConnectionRunnable task, long timeout, TimeUnit unit )
        throws InterruptedException
    {
        taskQueue.offer( task, timeout, unit );
    }
}