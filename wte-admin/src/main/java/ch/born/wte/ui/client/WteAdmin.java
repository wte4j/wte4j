package ch.born.wte.ui.client;

import ch.born.wte.ui.client.templates.TemplateListPresenter;
import ch.born.wte.ui.client.templates.TemplateListPanel;

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
		TemplateListPanel display=new TemplateListPanel();	
		
		TemplateListPresenter presenter=new TemplateListPresenter();
		presenter.bindTo(display);		
		
		MainPanel mainPanel=new MainPanel();
		mainPanel.setContent(display);

		RootPanel root = RootPanel.get("wte4j-admin");
		root.add(mainPanel);
		
		presenter.loadData();
	}
}
