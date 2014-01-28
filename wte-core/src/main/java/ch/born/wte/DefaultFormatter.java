package ch.born.wte;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation, which marks a {@link Formatter} as default for objects of a given
 * class. The Formatter must have a default constructor.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultFormatter {
	Class<?>[] value();
}
