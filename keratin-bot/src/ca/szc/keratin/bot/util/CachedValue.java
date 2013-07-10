package ca.szc.keratin.bot.util;

public abstract class CachedValue<T>
{
    private long lastUpdateMillis;

    private long expiryMillis = expiryMillis();

    private T cachedValue = null;

    public synchronized T getValue()
    {
        if ( cachedValue == null || System.currentTimeMillis() >= lastUpdateMillis + expiryMillis )
        {
            cachedValue = updateValue();
            lastUpdateMillis = System.currentTimeMillis();
        }

        return cachedValue;
    }

    protected void setExpiryMillis( long expiryMillis )
    {
        this.expiryMillis = expiryMillis;
    }

    protected abstract T updateValue();

    protected abstract long expiryMillis();
}