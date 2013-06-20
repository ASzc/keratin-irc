package ca.szc.keratin.bot.misc;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.LoggingLevel;

public class Logging
{

    /**
     * Set tinylog's logging level and output format.
     */
    public static void activateDefaultLoggingConfig()
    {
        String formatPattern = "{date:yyyy-MM-dd HH:mm:ss.SSS} [{thread}] {class}.{method}()  {level}: {message}";
        Configurator.defaultConfig().level( LoggingLevel.TRACE ).formatPattern( formatPattern ).activate();
    }

}
