/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.net;

import org.pmw.tinylog.Logger;

import net.engio.mbassy.common.DeadMessage;
import net.engio.mbassy.listener.Handler;

/**
 * Logs DeadMessages at trace level
 */
public class DeadMessageHandler
{

    @Handler
    private void logDeadMessage( DeadMessage msg )
    {
        Logger.trace( "Unhandled message: '" + msg.getMessage() + "'" );
    }

}
