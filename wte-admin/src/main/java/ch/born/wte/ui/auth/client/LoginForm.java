package ch.born.wte.ui.auth.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LoginForm extends Composite {

	private static LoginPageUiBinder uiBinder = GWT
			.create(LoginPageUiBinder.class);


	public LoginForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	interface LoginPageUiBinder extends UiBinder<Widget, LoginForm> {
	}
}
