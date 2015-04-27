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

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.wte4j.examples.showcase.client.Application;
import org.wte4j.examples.showcase.client.generation.GenerateDocumentDisplay;
import org.wte4j.examples.showcase.client.generation.GenerateDocumentPanel;
import org.wte4j.examples.showcase.client.generation.GenerateDocumentPresenter;
import org.wte4j.examples.showcase.client.management.TemplateManagerDisplay;
import org.wte4j.examples.showcase.client.management.TemplateManagerPanel;
import org.wte4j.examples.showcase.client.management.TemplateManagerPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;

public class MainPresenter {
	private MainDisplay mainDisplay;
	private TemplateManagerDisplay templateManagerDisplay;
	private GenerateDocumentDisplay generateDocumentDisplay;

	public void bind(MainDisplay aDisplay) {
		if (mainDisplay != null) {
			throw new IllegalStateException("Presenter allready bound");
		}
		mainDisplay = aDisplay;

		initGenerateDocument();
		initManageTemplate();
		bindGenerateDocumentAction();
		bindManageTemplateAction();

	}

	private void initGenerateDocument() {
		generateDocumentDisplay = new GenerateDocumentPanel();
		GenerateDocumentPresenter contentPresenter = new GenerateDocumentPresenter();
		contentPresenter.bind(generateDocumentDisplay);
	}

	private void initManageTemplate() {
		templateManagerDisplay = new TemplateManagerPanel();
		TemplateManagerPresenter contentPresenter = new TemplateManagerPresenter();
		contentPresenter.bind(templateManagerDisplay);
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
		mainDisplay.setMainContent(templateManagerDisplay);
		IsWidget helpText = generateHelpText(Application.MESSAGES.wte4j_message_template_manager_helptext());
		mainDisplay.setRightContent(helpText);
		mainDisplay.setGenerateDocumentActive(false);
		mainDisplay.setManageTemplatesActive(true);
	}

	public void displayGenerateDocumentContent() {
		mainDisplay.setMainContent(generateDocumentDisplay);
		IsWidget helpText = generateHelpText(Application.MESSAGES.wte4j_message_document_generator_helptext());
		mainDisplay.setRightContent(helpText);
		mainDisplay.setGenerateDocumentActive(true);
		mainDisplay.setManageTemplatesActive(false);
	}

	private IsWidget generateHelpText(
			String helpText) {
		FlowPanel helpTextcontainer = new FlowPanel();
		String[] htmlHelpText = helpText.split("\n");
		for (String line : htmlHelpText) {
			helpTextcontainer.add(new Paragraph(line));
		}
		return helpTextcontainer;
	}
}
