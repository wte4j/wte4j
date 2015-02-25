package ch.born.wte.ui.client;

import ch.born.wte.ui.client.templates.TemplateTablePanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WteAdmin implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel root = RootPanel.get();
		MainPanel main = new MainPanel();
		root.add(main);

		TemplateTablePanel templateListPanel = new TemplateTablePanel();
		main.addContent(templateListPanel);
		templateListPanel.loadData();

	}
}
