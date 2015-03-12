package ch.born.wte.ui.auth.client;

import com.google.gwt.user.client.ui.PopupPanel;

public class LoginPage extends PopupPanel {

	public LoginPage() {
		LoginForm loginForm = new LoginForm();
		
		add(loginForm);
		center();
		setGlassEnabled(true);
	}
}
