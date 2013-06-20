/**
 * Copyright (C) 2013 Alexander Szczuczko
 *
 * This file may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package ca.szc.keratin.bot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation on blank non-static KeratinBot field(s) in handler classes to set that field to the bot that
 * HandlerContainer assigns the class instance to.
 */
@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface AssignedBot
{
}
