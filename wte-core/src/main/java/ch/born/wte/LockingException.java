package ch.born.wte;

/**
 * * Exception is thrown, when a template is locked by an other user.
 */
public class LockingException extends RuntimeException {

	public LockingException(String message) {
		super(message);
	}

	public LockingException(String message, Throwable cause) {
		super(message, cause);
	}

}
