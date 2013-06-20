package ca.szc.keratin.core.net.io;

import java.nio.charset.Charset;

/**
 * Contains static input/output configuration
 */
public class IoConfig
{

    private IoConfig()
    {
    }

    public static final Charset CHARSET = Charset.forName( "UTF-8" );

    public static final int WAIT_TIME = 5000;

}