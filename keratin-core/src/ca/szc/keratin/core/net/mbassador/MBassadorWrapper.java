package ca.szc.keratin.core.net.mbassador;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import net.engio.mbassy.IPublicationErrorHandler;
import net.engio.mbassy.bus.BusConfiguration;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.MessagePublication;
import net.engio.mbassy.listener.Handler;

import org.pmw.tinylog.Logger;

import ca.szc.keratin.core.event.BusWatchdogEvent;
import ca.szc.keratin.core.event.IrcEvent;

/**
 * Wraps a MBassador object, so it can be replaced when it breaks without leaving stale references. Uses a watch-dog
 * thread to detect when message delivery stops working. Automatically replaces the broken MBassador when this occurs.
 */
public class MBassadorWrapper
{
    private MBassador<IrcEvent> delegate;

    private final BusConfiguration configuration;

    private final Set<Object> listeners;

    private final Set<IPublicationErrorHandler> errorHandlers;

    public MBassadorWrapper( BusConfiguration configuration )
    {
        this.configuration = configuration;

        listeners = new HashSet<Object>();
        errorHandlers = new HashSet<IPublicationErrorHandler>();

        rebootDelegate();

        Thread wd = new Thread()
        {
            private Semaphore activityMonitor = new Semaphore( 0 );

            private Semaphore watchdogMonitor = new Semaphore( 0 );

            public void run()
            {
                Logger.trace( "Bus watchdog running" );
                Thread.currentThread().setName( "BusWatchdog" );

                while ( !Thread.interrupted() )
                {
                    try
                    {
                        boolean activityTimedout = !activityMonitor.tryAcquire( 30, TimeUnit.SECONDS );
                        if ( activityTimedout )
                        {
                            Logger.trace( "No activity, checking if the bus is alive" );
                            publish( new BusWatchdogEvent( null ) );
                            boolean watchdogTimedout = !activityMonitor.tryAcquire( 5, TimeUnit.SECONDS );
                            if ( watchdogTimedout )
                            {
                                Logger.trace( "The bus is not alive, rebooting the bus" );
                                rebootDelegate();
                            }
                            else
                            {
                                Logger.trace( "The bus is alive" );
                            }
                        }
                    }
                    catch ( InterruptedException e )
                    {
                        break;
                    }
                }
                Logger.trace( "Interrupted, exiting" );
            };

            @Handler
            public void activityMonitor( IrcEvent event )
            {
                activityMonitor.release();
            }

            @Handler
            public void watchdogMonitor( BusWatchdogEvent event )
            {
                watchdogMonitor.release();
            }
        };
        subscribe( wd );
        wd.start();
    }

    private synchronized void rebootDelegate()
    {
        // Make sure old delegate is disposed of
        if ( delegate != null )
            delegate.shutdown();

        // New delegate
        delegate = new MBassador<IrcEvent>( configuration );

        // Add the active listeners and handler the old delegate had.
        for ( Object listener : listeners )
            delegate.subscribe( listener );
        for ( IPublicationErrorHandler handler : errorHandlers )
            delegate.addErrorHandler( handler );
    }

    public synchronized void addErrorHandler( IPublicationErrorHandler handler )
    {
        delegate.addErrorHandler( handler );
    }

    public synchronized void publish( IrcEvent message )
    {
        delegate.publish( message );
    }

    public synchronized MessagePublication publishAsync( IrcEvent message )
    {
        return delegate.publishAsync( message );
    }

    public synchronized void shutdown()
    {
        delegate.shutdown();
    }

    public synchronized void subscribe( Object listener )
    {
        delegate.subscribe( listener );
        listeners.add( listener );
    }

    public synchronized boolean unsubscribe( Object listener )
    {
        listeners.remove( listener );
        return delegate.unsubscribe( listener );
    }
}
