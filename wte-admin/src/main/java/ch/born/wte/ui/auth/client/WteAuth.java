package ch.born.wte.ui.auth.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WteAuth implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {		
		
		LoginPage loginPage = new LoginPage();
		
		RootPanel root= RootPanel.get();
		root.add(loginPage);
	}
}
