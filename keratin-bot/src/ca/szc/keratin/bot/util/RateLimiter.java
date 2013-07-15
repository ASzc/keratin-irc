package ca.szc.keratin.bot.util;

import java.util.concurrent.TimeUnit;

public class RateLimiter
{
    private long lastSuccessMillis;

    private final long cooldownMillis;

    public RateLimiter( long cooldown, TimeUnit cooldownUnit )
    {
        this( cooldownUnit.toMillis( cooldown ) );
    }

    public RateLimiter( long cooldownMillis )
    {
        this.cooldownMillis = cooldownMillis;
    }

    /**
     * Has the cooldown time expired? Never restarts the cooldown.
     * 
     * @return true iff the current time is greater than the last successful call time plus the cooldown time.
     */
    public boolean peek()
    {
        synchronized ( this )
        {
            return System.currentTimeMillis() > lastSuccessMillis + cooldownMillis;
        }
    }

    /**
     * Has the cooldown time expired? When returning true, the cooldown is started again.
     * 
     * @return true iff the current time is greater than the last successful call time plus the cooldown time.
     */
    public boolean available()
    {
        synchronized ( this )
        {
            if ( peek() )
            {
                lastSuccessMillis = System.currentTimeMillis();
                return true;
            }
            return false;
        }
    }
}
