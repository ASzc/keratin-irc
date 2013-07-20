package ca.szc.keratin.core.net.message;

/**
 * Assists in the construction of standard IrcMessage instances
 */
public class IrcMessages
{
    public static final String INVITE_COMMAND = "INVITE";

    /**
     * Creates an <a href="https://tools.ietf.org/html/rfc1459.html#section-4.2.7">INVITE</a> message
     * 
     * @param nick
     * @param channel
     * @return An IrcMessage of the specified type with the given parameters
     * @throws InvalidMessageParamException If one of the given parameters fails validation
     */
    public static IrcMessage invite( String nick, String channel )
        throws InvalidMessageParamException
    {
        try
        {
            return new IrcMessage( null, INVITE_COMMAND, nick, channel );
        }
        catch ( InvalidMessagePrefixException | InvalidMessageCommandException e )
        {
            throw new BadStandardValues( e );
        }
    }

    // TODO more...

    public static class BadStandardValues
        extends RuntimeException
    {
        private static final long serialVersionUID = -5190231809043802158L;

        public BadStandardValues( Throwable cause )
        {
            super( "The constants in this class should always be accepted, but they have been rejected", cause );
        }
    }
}
