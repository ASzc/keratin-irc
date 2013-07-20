package ca.szc.keratin.core.net.mbassador;

import net.engio.mbassy.dispatch.EnvelopedMessageDispatcher;
import net.engio.mbassy.dispatch.FilteredMessageDispatcher;
import net.engio.mbassy.dispatch.IHandlerInvocation;
import net.engio.mbassy.dispatch.IMessageDispatcher;
import net.engio.mbassy.subscription.SubscriptionContext;
import net.engio.mbassy.subscription.SubscriptionFactory;

public class TimeoutSubscriptionFactory
    extends SubscriptionFactory
{
    // Super doesn't specify types, can't do anything to correct that and still override
    @SuppressWarnings( "rawtypes" )
    @Override
    protected IMessageDispatcher buildDispatcher( SubscriptionContext context, IHandlerInvocation invocation )
    {
        IMessageDispatcher dispatcher = new TimeoutMessageDispatcher( context, invocation );
        if ( context.getHandlerMetadata().isEnveloped() )
        {
            dispatcher = new EnvelopedMessageDispatcher( dispatcher );
        }
        if ( context.getHandlerMetadata().isFiltered() )
        {
            dispatcher = new FilteredMessageDispatcher( dispatcher );
        }
        return dispatcher;
    }

    // TODO override actual handler maker thing, so we target individual methods instead of whole container classes?
}
