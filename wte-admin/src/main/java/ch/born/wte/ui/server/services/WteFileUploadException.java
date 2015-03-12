package ch.born.wte.ui.server.services;

public class WteFileUploadException extends RuntimeException {

	private MessageKey messageKey;

	public WteFileUploadException(MessageKey messageKey) {
		super(messageKey.getValue());
		this.messageKey = messageKey;
	}

	public WteFileUploadException(MessageKey messageKey, Throwable cause) {
		super(messageKey.getValue(), cause);
		this.messageKey = messageKey;
	}

	public WteFileUploadException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageKey getMessageKey() {
		return messageKey;
	}
}
