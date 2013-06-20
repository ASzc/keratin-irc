/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
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
