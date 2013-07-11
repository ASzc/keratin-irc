/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot.misc;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.LoggingLevel;

/**
 * Set tinylog's logging level and output format.
 */
public class Logging
{
    /**
     * Configure at the given logging level
     */
    public static void activateLoggingConfig( LoggingLevel logLevel )
    {
        String formatPattern = "{date:yyyy-MM-dd HH:mm:ss.SSS} [{thread}] {class}.{method}()  {level}: {message}";
        Configurator.defaultConfig().level( logLevel ).formatPattern( formatPattern ).activate();
    }

    /**
     * Configure at TRACE logging level
     */
    public static void activateDebugLoggingConfig()
    {
        activateLoggingConfig( LoggingLevel.TRACE );
    }
}
