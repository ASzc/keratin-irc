/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot;

import java.util.LinkedList;
import java.util.List;

import ca.szc.keratin.bot.KeratinBot.Channel;

/**
 * Facilitates easier management of multiple bots
 */
public class MultiBotManager
{

    private String user;

    private String nick;

    private String realName;

    private String serverAddress;

    private int serverPort;

    private boolean sslEnabled;

    private Channel[] initialChannels;

    private final List<KeratinBot> botList;

    /**
     * Init with no default values.
     */
    public MultiBotManager()
    {
        botList = new LinkedList<KeratinBot>();
    }

    /**
     * Init with default values.
     * 
     * @param user
     * @param nick
     * @param realName
     * @param serverAddress
     * @param serverPort
     * @param sslEnabled
     */
    public MultiBotManager( String user, String nick, String realName, String serverAddress, int serverPort,
                            boolean sslEnabled, String[] initialChannels )
    {
        this();
        setUser( user );
        setNick( nick );
        setRealName( realName );
        setServerAddress( serverAddress );
        setServerPort( serverPort );
        setSslEnabled( sslEnabled );
    }

    /**
     * Create a new bot using default values.
     * 
     * @return A reference to the new bot, also stored internally.
     * @throws NullPointerException if one or more parameters have no defined default values
     */
    public KeratinBot newBot()
    {
        return newBot( user, nick, realName, serverAddress, serverPort, sslEnabled, initialChannels );
    }

    /**
     * Create a new bot without using default values.
     * 
     * @return A reference to the new bot, also stored internally.
     */
    public KeratinBot newBot( String user, String nick, String realName, String serverAddress, int serverPort,
                              boolean sslEnabled, Channel[] initialChannels )
    {
        KeratinBot bot = new KeratinBot( user, nick, realName, serverAddress, serverPort, sslEnabled, initialChannels );

        botList.add( bot );

        return bot;
    }

    /**
     * Call connect() on all established bots.
     */
    public void connectAll()
    {
        for ( KeratinBot bot : botList )
        {
            bot.connect();
        }
    }

    public String getNick()
    {
        return nick;
    }

    public String getRealName()
    {
        return realName;
    }

    public String getServerAddress()
    {
        return serverAddress;
    }

    public int getServerPort()
    {
        return serverPort;
    }

    public String getUser()
    {
        return user;
    }

    public boolean isSslEnabled()
    {
        return sslEnabled;
    }

    public void setNick( String nick )
    {
        this.nick = nick;
    }

    public void setRealName( String realName )
    {
        this.realName = realName;
    }

    public void setServerAddress( String serverAddress )
    {
        this.serverAddress = serverAddress;
    }

    public void setServerPort( int serverPort )
    {
        this.serverPort = serverPort;
    }

    public void setSslEnabled( boolean sslEnabled )
    {
        this.sslEnabled = sslEnabled;
    }

    public void setUser( String user )
    {
        this.user = user;
    }

    public Channel[] getInitialChannels()
    {
        return initialChannels;
    }

    public void setInitialChannels( Channel[] initialChannels )
    {
        this.initialChannels = initialChannels;
    }

}
