package ch.born.wte.ui.server.services;

public enum MessageKey {
	INTERNAL_SERVER_ERROR,
	LOCKED_TEMPLATE,
	TEMPLATE_NOT_FOUND,
	TEMPLATE_UPLOADED,
	TEMPLATE_CLASS_NOT_FOUND,
	UPLOADED_FILE_NOT_READABLE,
	UPLOADED_FILE_NOT_VALID, 
	UPLOADED_FILE_TOO_LARGE; 

	private static final String WTE4J_MESSAGE_BASE = "wte4j.message.";
	private final String value;

	private MessageKey() {
		value = WTE4J_MESSAGE_BASE + this.name().toLowerCase();
	}

	public String getValue() {
		return value;
	}
}
