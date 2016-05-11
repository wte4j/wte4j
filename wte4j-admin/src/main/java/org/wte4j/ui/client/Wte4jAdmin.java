/**
 * Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
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
package org.wte4j.ui.client;

import org.wte4j.ui.client.templates.TemplateListPanel;
import org.wte4j.ui.client.templates.TemplateListPresenter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Wte4jAdmin implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel root = RootPanel.get("wte4j-admin");
		if (root != null) {
			TemplateListPanel display = new TemplateListPanel();

			TemplateListPresenter presenter = new TemplateListPresenter();
			presenter.bindTo(display);

			MainPanel mainPanel = new MainPanel();
			mainPanel.setContent(display);

			root.add(mainPanel);

			presenter.loadData();
		}
	}
}
