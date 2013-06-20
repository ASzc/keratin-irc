/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.net.message;

/**
 * Thrown for invalid IRC message commands
 */
public class InvalidMessageCommandException
    extends InvalidMessageException
{

    private static final long serialVersionUID = 2772320844451892354L;

    public InvalidMessageCommandException()
    {
        super();
    }

    public InvalidMessageCommandException( String message )
    {
        super( message );
    }

    public InvalidMessageCommandException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public InvalidMessageCommandException( Throwable cause )
    {
        super( cause );
    }

    protected InvalidMessageCommandException( String message, Throwable cause, boolean enableSuppression,
                                             boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
