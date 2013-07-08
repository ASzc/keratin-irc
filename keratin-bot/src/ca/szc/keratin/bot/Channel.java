package ca.szc.keratin.bot;

/**
 * Holds data for one channel
 */
public class Channel
{
    private final String name;

    private final String key;

    /**
     * @param name The channel's name. Including the #. Cannot be null.
     * @param key The channel's key. May be null if there is no key.
     */
    public Channel( String name, String key )
    {
        this.name = name;
        this.key = key;
    }

    /**
     * @return The channel's name. Including the #. Cannot be null.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The channel's key. May be null if there is no key.
     */
    public String getKey()
    {
        return key;
    }

    @Override
    public String toString()
    {
        return "Channel [name=" + name + ", key=" + key + "]";
    }
}