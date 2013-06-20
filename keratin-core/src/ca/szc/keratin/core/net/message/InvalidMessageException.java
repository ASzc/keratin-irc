package ca.szc.keratin.core.net.message;

/**
 * Thrown for invalid IRC messages
 */
public class InvalidMessageException
    extends Exception
{

    private static final long serialVersionUID = -851963659547885319L;

    public InvalidMessageException()
    {
        super();
    }

    public InvalidMessageException( String message )
    {
        super( message );
    }

    public InvalidMessageException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public InvalidMessageException( Throwable cause )
    {
        super( cause );
    }

    protected InvalidMessageException( String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
