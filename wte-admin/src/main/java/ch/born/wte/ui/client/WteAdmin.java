/**
 * Copyright (C) 2015 Born Informatik AG (www.born.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
