package ch.born.wte;

/**
 * This exception is thrown when the WTE does not find the implementation of
 * the formatter for the given name.
 */
public class UnknownFormatterException extends IllegalArgumentException {

	public UnknownFormatterException(String formatterName) {
		super("Formatter with name " + formatterName + "is unknown");
	}

}
