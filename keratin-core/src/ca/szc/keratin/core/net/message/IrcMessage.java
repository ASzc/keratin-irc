package ca.szc.keratin.core.net.message;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents, parses and provides general format verification of IRC message lines, as defined by RFC 1459.
 */
public class IrcMessage
{

    /**
     * Pattern that will match a DNS host name (ASCII only). Not perfect.
     */
    private static final Pattern hostPat =
        Pattern.compile( "(?:(?:\\p{Alpha}|\\p{Digit}|-){1,63}\\.)*(?:\\p{Alpha}|\\p{Digit}|-){1,63}" );

    /**
     * Pattern that will match a nick name (Extended charset)
     */
    private static final Pattern nickPat = Pattern.compile( "\\p{IsAlphabetic}(?:\\p{IsAlphabetic}|\\d|\\p{Punct})*" );

    /**
     * Pattern that will match a RFC 1459 message prefix, not including the leading colon.
     */
    // <prefix> ::= <servername> | <nick> [ '!' <user> ] [ '@' <host> ]
    private static final Pattern prefixPattern = Pattern.compile( "(?:" + hostPat + "|" + nickPat
        + "(?:!\\S\\S*){0,1}(?:@" + hostPat + "){0,1})" );

    /**
     * Pattern that will match a RFC 1459 message command, or a reply "command" number.
     */
    // <command> ::= <letter> { <letter> } | <number> <number> <number>
    private static final Pattern commandPattern = Pattern.compile( "(?:\\p{IsAlphabetic}+|\\d\\d\\d)" );

    /**
     * Pattern that will match a RFC 1459 middle parameter. Middle parameters aren't allowed spaces or a leading colon.
     */
    // <middle> ::= <Any *non-empty* sequence of octets not including SPACE
    // or NUL or CR or LF, the first of which may not be ':'>
    private static final Pattern middleParamPattern = Pattern.compile( "[^: ][^ \r\n]*" );

    /**
     * Pattern that will match a RFC 1459 trailing parameter. Trailing parameters are allowed spaces, and should have a
     * leading colon. It is feasible for the colon to not be required when the trailing parameter has no spaces.
     */
    // <trailing> ::= <Any, possibly *empty*, sequence of octets not including
    // NUL or CR or LF>
    private static final Pattern trailingParamPattern = Pattern.compile( ":[^\r\n]*" );

    /**
     * Pattern that will match a RFC 1459 message line. Group 1 contains the prefix, group 2 contains the command, and
     * group 3 contains any parameters, as a blob.
     */
    // <message> ::= [':' <prefix> <SPACE> ] <command> <params> <crlf>
    // <params> ::= <SPACE> [ ':' <trailing> | <middle> <params> ]
    private static final Pattern parseLinePattern = Pattern.compile( "(?::(" + prefixPattern + ") |)(" + commandPattern
        + ")((?: " + middleParamPattern + ")* (?:" + trailingParamPattern + "))+" );

    /**
     * Pattern that will match either a middle or trailing parameter, storing it in group 1. Best used with find()
     * rather than matches().
     */
    private static final Pattern parseLineParamsPattern = Pattern.compile( " (" + middleParamPattern + "|"
        + trailingParamPattern + ")" );

    /**
     * The RFC 1459 message prefix
     */
    private final String prefix;

    /**
     * The RFC 1459 message command
     */
    private final String command;

    /**
     * The RFC 1459 message parameters
     */
    private final String[] params;

    // Validating based on RFC 1459, grammar excerpt follows:
    //
    // The BNF representation for this is:
    //
    // <message> ::= [':' <prefix> <SPACE> ] <command> <params> <crlf>
    // <prefix> ::= <servername> | <nick> [ '!' <user> ] [ '@' <host> ]
    // <command> ::= <letter> { <letter> } | <number> <number> <number>
    // <SPACE> ::= ' ' { ' ' }
    // <params> ::= <SPACE> [ ':' <trailing> | <middle> <params> ]
    //
    // <middle> ::= <Any *non-empty* sequence of octets not including SPACE
    // or NUL or CR or LF, the first of which may not be ':'>
    // <trailing> ::= <Any, possibly *empty*, sequence of octets not including
    // NUL or CR or LF>
    //
    // <crlf> ::= CR LF
    //
    // <target> ::= <to> [ "," <target> ]
    // <to> ::= <channel> | <user> '@' <servername> | <nick> | <mask>
    // <channel> ::= ('#' | '&') <chstring>
    // <servername> ::= <host>
    // <host> ::= see RFC 952 [DNS:4] for details on allowed hostnames
    // <nick> ::= <letter> { <letter> | <number> | <special> }
    // <mask> ::= ('#' | '$') <chstring>
    // <chstring> ::= <any 8bit code except SPACE, BELL, NUL, CR, LF and
    // comma (',')>
    //
    // Other parameter syntaxes are:
    //
    // <user> ::= <nonwhite> { <nonwhite> }
    // <letter> ::= 'a' ... 'z' | 'A' ... 'Z'
    // <number> ::= '0' ... '9'
    // <special> ::= '-' | '[' | ']' | '\' | '`' | '^' | '{' | '}'

    /**
     * Create a RFC 1459 message. See the RFC's section 2.3.1 for a detailed specification.
     * 
     * @param prefix The prefix component, may be null.
     * @param command The command component, may not be null, may not be zero length.
     * @param params The params component, may be zero length, no element may be null.
     * @throws InvalidMessagePrefixException If an invalid prefix component is given.
     * @throws InvalidMessageCommandException If an invalid command component is given.
     * @throws InvalidMessageParamException If one of the parts of the given params component is invalid.
     */

    /**
     * Create IRC message from its three main components.
     * 
     * @param prefix The message prefix, may be null.
     * @param command The command
     * @param params The parameters, may be zero-length.
     * @throws InvalidMessagePrefixException If the prefix is invalid
     * @throws InvalidMessageCommandException If the command is invalid
     * @throws InvalidMessageParamException If a parameter is invalid
     */
    // <message>
    public IrcMessage( String prefix, String command, String... params )
        throws InvalidMessagePrefixException, InvalidMessageCommandException, InvalidMessageParamException
    {
        // <prefix>
        if ( prefix != null )
        {
            if ( prefix.length() <= 0 )
                throw new InvalidMessagePrefixException( "Must be greater than zero length" );

            if ( !prefixPattern.matcher( prefix ).matches() )
                throw new InvalidMessagePrefixException( "Did not match pattern" );
        }

        // <command>
        if ( command.length() <= 0 )
            throw new InvalidMessageCommandException( "Must be greater than zero length" );
        if ( !commandPattern.matcher( command ).matches() )
            throw new InvalidMessageCommandException( "Did not match pattern" );

        // <params>
        for ( int i = 0; i < params.length; i++ )
        {
            String param = params[i];

            if ( i == params.length - 1 )
            {
                // <trailing>
                if ( param.length() == 0 || param.charAt( 0 ) != ':' )
                {
                    param = ":" + param;
                    params[i] = param;
                }
                if ( !trailingParamPattern.matcher( param ).matches() )
                    throw new InvalidMessageParamException( "Did not match pattern" );
            }
            else
            {
                // <middle>
                if ( param.length() <= 0 )
                    throw new InvalidMessageParamException( "Must be greater than zero length" );
                if ( !middleParamPattern.matcher( param ).matches() )
                    throw new InvalidMessageParamException( "Did not match pattern" );
            }
        }

        this.prefix = prefix;
        this.command = command;
        this.params = params;
    }

    /**
     * Parse a raw IRC message into an IrcMessage
     * 
     * @param line The raw line of an IRC message
     * @return An IrcMessage representation of line
     * @throws InvalidMessageException
     */
    public static IrcMessage parseMessage( String line )
        throws InvalidMessageException
    {
        IrcMessage retMsg;

        Matcher matcher = parseLinePattern.matcher( line );
        if ( matcher.matches() )
        {

            String prefix = matcher.group( 1 );
            // prefix is null if not present

            String command = matcher.group( 2 );

            String[] params = parseMessageParams( matcher.group( 3 ) );

            retMsg = new IrcMessage( prefix, command, params );
        }
        else
        {
            throw new InvalidMessageException( "Message didn't match pattern" );
        }

        return retMsg;
    }

    /**
     * Parse a parameter blob into parameters.
     * 
     * @param paramBlob A bunch of parameters in one string
     * @return Seperated parameters. Last parameter is the trailing parameter, and may have spaces.
     * @throws InvalidMessageException If no parameters were matched
     */
    private static String[] parseMessageParams( String paramBlob )
        throws InvalidMessageException
    {
        Matcher matcher = parseLineParamsPattern.matcher( paramBlob );

        LinkedList<String> params = new LinkedList<String>();

        while ( matcher.find() )
        {
            params.add( matcher.group( 1 ) );
        }

        if ( params.size() < 1 )
        {
            throw new InvalidMessageException( "Message parameters didn't match pattern" );
        }

        String[] retArray = new String[params.size()];
        params.toArray( retArray );

        return retArray;
    }

    /**
     * Compares some text against the nick validation pattern.
     * 
     * @param text Text to validate as a nick
     * @return true iff text matches the nick pattern
     */
    public static boolean isNick( String text )
    {
        return nickPat.matcher( text ).matches();
    }

    /**
     * Compares some text against the host validation pattern.
     * 
     * @param text Text to validate as a host
     * @return true iff text matches the host pattern
     */
    public static boolean isHost( String text )
    {
        return hostPat.matcher( text ).matches();
    }

    /**
     * Get the RFC 1459 message prefix component, if it exists.
     * 
     * @return A string if there is a prefix, otherwise null.
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * Get the RFC 1459 message command component.
     * 
     * @return A string always.
     */
    public String getCommand()
    {
        return command;
    }

    /**
     * Get the RFC 1459 message params component.
     * 
     * @return An array of parameter strings, which is zero length if there are no parameters.
     */
    public String[] getParams()
    {
        return params;
    }

    /**
     * Returns the message as a raw one-line IRC protocol message String
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if ( prefix != null )
        {
            sb.append( prefix );
            sb.append( " " );
        }
        sb.append( command );

        for ( String param : params )
        {
            sb.append( " " );
            sb.append( param );
        }
        // sb.append( "\n" );
        return sb.toString();
    }
}
