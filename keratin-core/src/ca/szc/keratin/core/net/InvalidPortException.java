/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.net;

/**
 * Thrown when a port number is invalid instead of the general unchecked exception thrown by Java.
 */
public class InvalidPortException
    extends Exception
{

    private static final long serialVersionUID = -4888932452832860111L;

    public InvalidPortException()
    {
        super();
    }

    public InvalidPortException( String message )
    {
        super( message );
    }

    public InvalidPortException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public InvalidPortException( Throwable cause )
    {
        super( cause );
    }

    protected InvalidPortException( String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
