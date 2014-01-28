package ch.born.wte;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definiert den Namen eines Formatters. Ist die Annotation nicht gesetzt, wird
 * der einfache Klassenamen verwendet.
 * 
 * @author ofreivogel
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormatterName {
	String value();
}
