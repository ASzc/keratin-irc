package ca.szc.keratin.core.net.message;

/**
 * Thrown for invalid IRC message prefixes
 */
public class InvalidMessagePrefixException
    extends InvalidMessageException
{

    private static final long serialVersionUID = 2772320844451892354L;

    public InvalidMessagePrefixException()
    {
        super();
    }

    public InvalidMessagePrefixException( String message )
    {
        super( message );
    }

    public InvalidMessagePrefixException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public InvalidMessagePrefixException( Throwable cause )
    {
        super( cause );
    }

    protected InvalidMessagePrefixException( String message, Throwable cause, boolean enableSuppression,
                                             boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
