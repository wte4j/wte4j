package ch.born.wte;

public class WteException extends RuntimeException {

	public WteException() {
		super();
	}

	public WteException(Throwable cause) {
		super(cause);
	}

	public WteException(String message) {
		super(message);
	}

	public WteException(String message, Throwable cause) {
		super(message, cause);
	}

}
