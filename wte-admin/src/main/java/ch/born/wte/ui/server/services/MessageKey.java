package ch.born.wte.ui.server.services;

public enum MessageKey {
	INTERNAL_SERVER_ERROR,
	LOCKED_TEMPLATE,
	TEMPLATE_NOT_FOUND,
	TEMPLATE_UPLOADED,
	UPLOADED_FILE_NOT_READABLE,
	UPLOADED_FILE_NOT_VALID;

	private final String value;

	private MessageKey() {
		value = this.name().toLowerCase();
	}

	public String getValue() {
		return value;
	}
}
