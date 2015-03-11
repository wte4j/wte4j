package ch.born.wte.ui.server.services;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.born.wte.User;

@Component
@Scope(value = "request")
public class SimpleServiceContext implements ServiceContext {

	@Override
	public User getCurrentUser() {
		return new User("anonymous", "anonymous");

	}

	@Override
	public Locale getCurrentLocale() {
		return Locale.ENGLISH;
	}

}
