package ch.born.wte.ui.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageFactoryImpl implements MessageFactory {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ServiceContext serviceContext;

	@Override
	public String createMessage(String key) {
		return messageSource.getMessage(key, null, "$" + key + "$", serviceContext.getLocale());
	}

	@Override
	public String createMessage(String key, Object... params) {
		return messageSource.getMessage(key, params, "$" + key + "$", serviceContext.getLocale());
	}

}
