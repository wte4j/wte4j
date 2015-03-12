package ch.born.wte.ui.server.services;

import java.util.Locale;

import ch.born.wte.User;

public interface ServiceContext {
	User getUser();

	void setUser(User user);

	Locale getLocale();

	void setLocale(Locale locale);
}
