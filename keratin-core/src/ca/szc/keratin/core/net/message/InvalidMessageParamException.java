/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.core.net.message;

/**
 * Thrown for invalid IRC message parameters
 */
public class InvalidMessageParamException
    extends InvalidMessageException
{

    private static final long serialVersionUID = 2772320844451892354L;

    public InvalidMessageParamException()
    {
        super();
    }

    public InvalidMessageParamException( String message )
    {
        super( message );
    }

    public InvalidMessageParamException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public InvalidMessageParamException( Throwable cause )
    {
        super( cause );
    }

    protected InvalidMessageParamException( String message, Throwable cause, boolean enableSuppression,
                                             boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
