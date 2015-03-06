package ch.born.wte.ui.server.services;

public class WteFileUploadException extends RuntimeException {

	public WteFileUploadException(String message) {
		super(message);
	}
	
	public WteFileUploadException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
