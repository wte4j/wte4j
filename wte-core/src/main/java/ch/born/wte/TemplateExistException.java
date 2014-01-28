package ch.born.wte;

public class TemplateExistException extends RuntimeException {

	public TemplateExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateExistException(String message) {
		super(message);
	}

}
