package ca.szc.keratin.core.net.mbassador;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.engio.mbassy.bus.MessagePublication;
import net.engio.mbassy.common.IConcurrentSet;
import net.engio.mbassy.dispatch.IHandlerInvocation;
import net.engio.mbassy.dispatch.MessageDispatcher;
import net.engio.mbassy.subscription.SubscriptionContext;

import org.pmw.tinylog.Logger;

public class TimeoutMessageDispatcher
    extends MessageDispatcher
{
    private static final ExecutorService service = Executors.newCachedThreadPool();

    // Super doesn't specify types, can't do anything to correct that
    @SuppressWarnings( "rawtypes" )
    public TimeoutMessageDispatcher( SubscriptionContext context, IHandlerInvocation invocation )
    {
        super( context, invocation );
    }

    // Super doesn't specify types, can't do anything to correct that
    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public void dispatch( MessagePublication publication, final Object message, IConcurrentSet listeners )
    {
        publication.markDelivered();
        for ( final Object listener : listeners )
        {
            Future future = service.submit( new Runnable()
            {
                @Override
                public void run()
                {
                    getInvocation().invoke( listener, message );
                }
            } );

            try
            {
                future.get( 10, TimeUnit.SECONDS );
            }
            catch ( InterruptedException e )
            {
                Thread.currentThread().interrupt();
            }
            catch ( ExecutionException e )
            {
                Logger.error( e, "Handler threw exception on invocation" );
            }
            catch ( TimeoutException e )
            {
                future.cancel( true );
                Logger.error( "Invocation timed out for handler in listener " + listener + " with message " + message );
            }
        }
    }
}
