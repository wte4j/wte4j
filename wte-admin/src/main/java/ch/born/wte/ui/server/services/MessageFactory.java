package ch.born.wte.ui.server.services;

public interface MessageFactory {
	public String createMessage(String key);

	public String createMessage(String key, Object... params);
}
