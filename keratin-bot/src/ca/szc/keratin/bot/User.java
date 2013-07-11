/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot;

/**
 * Holds data for one user
 */
public class User
{
    private final String nick;

    private PrivLevel privLevel;

    public User( String nick )
    {
        this( nick, PrivLevel.Regular );
    }

    public User( String nick, PrivLevel privLevel )
    {
        this.nick = nick;
        this.privLevel = privLevel;
    }

    public PrivLevel getPrivLevel()
    {
        return privLevel;
    }

    public void setPrivLevel( PrivLevel privLevel )
    {
        this.privLevel = privLevel;
    }

    public String getNick()
    {
        return nick;
    }

    @Override
    public String toString()
    {
        return "User [nick=" + nick + ", privLevel=" + privLevel + "]";
    }

    /**
     * IRC Privilege level relative to the parent Channel
     */
    public enum PrivLevel
    {
        /**
         * Just a regular unprivileged user
         */
        Regular,

        /**
         * A user with op permissions
         */
        Op
    }
}
