package ch.born.wte;

/**
 * Exception is thrown, when the WTE can't create the requested formater
 * instance.
 * 
 * @author ofreivogel
 * 
 */
public class FormatterInstantiationException extends IllegalArgumentException {

	public FormatterInstantiationException(String message, Throwable cause) {
		super(message, cause);
	}

	public FormatterInstantiationException(String s) {
		super(s);
	}

}
