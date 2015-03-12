package ch.born.wte.ui.server.services;

import ch.born.wte.User;


public interface ServiceContext {
	User getUser();
	void setUser(User user);
}
