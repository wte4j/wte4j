package ch.born.wte.ui.server.services;

import java.util.Locale;

import ch.born.wte.User;

public interface ServiceContext {
	User getCurrentUser();

	Locale getCurrentLocale();
}
