package ch.born.wte.ui.auth.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LoginPage extends Composite {

	private static LoginPageUiBinder uiBinder = GWT
			.create(LoginPageUiBinder.class);


	public LoginPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	interface LoginPageUiBinder extends UiBinder<Widget, LoginPage> {
	}
}
