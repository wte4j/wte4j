package ch.born.wte.ui.server.services;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.born.wte.User;

@Component
@Scope(value = "request")
public class SimpleServiceContext implements ServiceContext {

	private User currentUser = new User("anonymous", "anonymous");
	private Locale currentLocale = Locale.ENGLISH;

	@Override
	public User getUser() {
		return currentUser;

	}

	@Override
	public void setUser(User user) {
		currentUser = user;
	}

	@Override
	public Locale getLocale() {
		return currentLocale;
	}

	@Override
	public void setLocale(Locale locale) {
		currentLocale = locale;

	}

}
