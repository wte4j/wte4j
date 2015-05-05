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
package org.wte4j.examples.showcase.client.management;

import org.wte4j.examples.showcase.client.Application;
import org.wte4j.ui.client.GrowlFactory;
import org.wte4j.ui.client.dialog.DialogType;
import org.wte4j.ui.client.dialog.MessageDialog;
import org.wte4j.ui.client.templates.TemplateListDisplay;
import org.wte4j.ui.client.templates.TemplateListPresenter;
import org.wte4j.ui.client.templates.mapping.MappingPanel;
import org.wte4j.ui.client.templates.upload.TemplateUploadFormPanel;
import org.wte4j.ui.shared.TemplateDto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;

public class TemplateManagerPresenter implements NewTemplateWizard.WizardContainer {

	private TemplateListPresenter displayTemplatesPresenter;

	private TemplateManagerDisplay display;
	private NewTemplateWizard newTemplateWizard = new NewTemplateWizard();

	public void bind(TemplateManagerDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException("presenter is already bound");
		}
		display = aDisplay;
		bindTemplateList();
		initAddTemplateButton();
		initNewTemplateWizard();
	}

	private void initNewTemplateWizard() {
		newTemplateWizard.bind(new TemplateUploadFormPanel("create-new-template-upload"));
		newTemplateWizard.bind(new NewTemplatePanel());
		newTemplateWizard.bind(new MappingPanel());
	}

	private void bindTemplateList() {
		TemplateListDisplay displayTemplatesDisplay = display.getTemplateListDisplay();
		displayTemplatesPresenter = new TemplateListPresenter();
		displayTemplatesPresenter.bindTo(displayTemplatesDisplay);
		displayTemplatesPresenter.loadData();
	}

	private void initAddTemplateButton() {
		display.addAddTemplateClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				showNewTemplateDialog();
			}
		});

		display.addCreateTemplateFromFile(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showNewTemplateDialogWithFileUpload();
			}
		});
	}

	void showNewTemplateDialog() {
		newTemplateWizard.startWithModel(this);
	}

	void showNewTemplateDialogWithFileUpload() {
		newTemplateWizard.startWihtFileUpload(this);
	}

	@Override
	public void displayWizardContent(IsWidget wiget) {
		display.setDialogContent(wiget, Application.LABELS.addTemplate());
	}

	@Override
	public void showErrorMessage(String message) {
		MessageDialog messageDialog = new MessageDialog("", message, DialogType.ERROR);
		messageDialog.show();
	}

	@Override
	public void onWizardStarted() {
		display.showAddTemplateDialog();
	}

	@Override
	public void onWizardCanceled() {
		display.hideAddTemplateDialog();
	}

	@Override
	public void onWizardCompleted(TemplateDto created) {
		display.hideAddTemplateDialog();
		GrowlFactory.success(Application.MESSAGES.wte4j_message_template_creation_success(created.getDocumentName()));
		displayTemplatesPresenter.loadData();
	}

}
