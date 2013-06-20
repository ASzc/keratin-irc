/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot.annotation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import org.pmw.tinylog.Logger;

import eu.infomas.annotation.AnnotationDetector;
import eu.infomas.annotation.AnnotationDetector.Reporter;
import eu.infomas.annotation.AnnotationDetector.TypeReporter;

/**
 * Finds classes annotated with the HandlerContainer annotation.
 */
public class HandlerContainerDetector
{
    private static List<Class<?>> containers;

    /**
     * Get classes annotated with the HandlerContainer annotation.
     * 
     * @return All HandlerContainer annotated classes
     * @see HandlerContainer
     */
    public static List<Class<?>> getContainers()
    {
        if ( containers == null )
        {
            final LinkedList<Class<?>> containersTemp = new LinkedList<Class<?>>();

            Reporter containerReporter = new TypeReporter()
            {
                @SuppressWarnings( "unchecked" )
                @Override
                public Class<? extends Annotation>[] annotations()
                {
                    return new Class[] { HandlerContainer.class };
                }

                @Override
                public void reportTypeAnnotation( Class<? extends Annotation> annotation, String className )
                {
                    try
                    {
                        containersTemp.add( Class.forName( className ) );
                    }
                    catch ( ClassNotFoundException e )
                    {
                        Logger.error( e, "Could not load detected annotated class" );
                    }
                }
            };

            AnnotationDetector containerDetector = new AnnotationDetector( containerReporter );
            try
            {
                containerDetector.detect();
            }
            catch ( IOException e )
            {
                Logger.error( e, "Problem when detecting annotations" );
            }

            containers = containersTemp;
        }

        return containers;
    }
}
