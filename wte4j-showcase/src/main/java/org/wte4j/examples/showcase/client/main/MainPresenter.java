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
package org.wte4j.examples.showcase.client.main;

import org.wte4j.examples.showcase.client.generation.GenerateDocumentDisplay;
import org.wte4j.examples.showcase.client.generation.GenerateDocumentPanel;
import org.wte4j.examples.showcase.client.generation.GenerateDocumentPresenter;
import org.wte4j.ui.client.templates.TemplateListPanel;
import org.wte4j.ui.client.templates.TemplateListPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class MainPresenter {
	private MainDisplay mainDisplay;

	public void bind(MainDisplay aDisplay) {
		if (mainDisplay != null) {
			throw new IllegalStateException("Presenter allready bound");
		}
		mainDisplay = aDisplay;

		bindGenerateDocumentAction();
		bindManageTemplateAction();

	}

	private void bindManageTemplateAction() {
		mainDisplay.addManageTemplateClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				displayTemplatesContent();

			}
		});
	}

	private void bindGenerateDocumentAction() {
		mainDisplay.addGenerateDocumentClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displayGenerateDocumentContent();
			}
		});
	}

	public void displayTemplatesContent() {
		TemplateListPanel displayTemplatesPanel = new TemplateListPanel();	
		TemplateListPresenter displayTemplatesPresenter=new TemplateListPresenter();
		displayTemplatesPresenter.bindTo(displayTemplatesPanel);
		
		mainDisplay.setMainContent(displayTemplatesPanel);
		mainDisplay.setRightContent(new Label("Helptext manage templates"));
		mainDisplay.setGenerateDocumentActive(false);
		mainDisplay.setManageTemplatesActive(true);
	}

	public void displayGenerateDocumentContent() {
		GenerateDocumentDisplay contentDisplay = new GenerateDocumentPanel();
		GenerateDocumentPresenter contentPresenter = new GenerateDocumentPresenter();
		contentPresenter.bind(contentDisplay);

		mainDisplay.setMainContent(contentDisplay);
		mainDisplay.setRightContent(new Label("Helptext generate documents"));
		mainDisplay.setGenerateDocumentActive(true);
		mainDisplay.setManageTemplatesActive(false);

	}
}
