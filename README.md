# Keratin Java IRC Bot and Client Framework

## Overview

Keratin is a MIT licensed IRC bot and client framework written for Java 7. It is currently in a workable alpha state.

MBassador is used for the event bus. Tinylog is used for the logging. Networking is performed through standard Java Sockets.

## One-file Bot Example

    package com.example.helloworld;

    import net.engio.mbassy.listener.Handler;
    import ca.szc.keratin.bot.KeratinBot;
    import ca.szc.keratin.bot.annotation.HandlerContainer;
    import ca.szc.keratin.core.event.message.recieve.ReceivePrivmsg;

    // This annotation causes the class to be scanned for @Handler methods at connect time.
    @HandlerContainer
    public class HelloBot
    {
        public static void main( String[] args )
        {
            // Long form init, it's also possible to do all of this in one line with a different constructor
            KeratinBot keratinBot = new KeratinBot();
            keratinBot.setUser( "HW" );
            keratinBot.setNick( "HelloWorld" );
            keratinBot.setRealName( "Hello World Test" );
            keratinBot.setServerAddress( "irc.freenode.net" );
            keratinBot.setServerPort( 6667 );
            keratinBot.setSslEnabled( false );
            keratinBot.addChannel( "#kertainexample" );

            // Does not block forever
            keratinBot.connect();

            // Main thread will end, but other threads will still be active
        }

        /**
         * Reply to !hello command with username: Hello, World!
         */
        // This annotation flags a method to be used by MBassador as an event handler.
        @Handler
        private void someHelloWorldMethod( ReceivePrivmsg event )
        {
            String text = event.getText();

            if ( "!hello".equals( text ) )
            {
                event.replyDirectly( "Hello, World!" );
            }
        }
    }

## How To Use

To use the bot framework as a maven dependency, place the following information in your pom.xml

      <dependencies>
        ...
        <dependency>
          <groupId>ca.szc.keratin</groupId>
          <artifactId>keratin-bot</artifactId>
          <version>0.0.1-SNAPSHOT</version>
        </dependency>
        ...
      </dependencies>

Or for just the core framework, use this information

      <dependencies>
        ...
        <dependency>
          <groupId>ca.szc.keratin</groupId>
          <artifactId>keratin-core</artifactId>
          <version>0.0.1-SNAPSHOT</version>
        </dependency>
        ...
      </dependencies>

Note that the artifacts are not yet available in maven central, so you must build them yourself before use.

## Build

Maven is used to get dependencies and to build. A standard maven install command will place built jars in your local repo.

    mvn install

If you just need jars, you can take them from the target/ directories after running the regular build command.

## Development

### Eclipse import

Install m2e, on Fedora Linux this is accomplished via:

    yum install eclipse-m2e-core

Reopen Eclipse if it is open, then use

    File > Import > Existing Maven Projects > Root Directory: select clone location

### Eclipse source formatting

Import the maven source style definitions by downloading

    http://maven.apache.org/developers/maven-eclipse-codestyle.xml

and then using

    Project > Properties > Java Code Style > Formatter > Enable Project Specific Settings > Import ...

### Pull Requests

Please submit pull requests from a personal development branch in your fork, not from master in your fork.
