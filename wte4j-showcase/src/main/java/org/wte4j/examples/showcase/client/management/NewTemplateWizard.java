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
package org.wte4j.examples.showcase.client.management;

import org.wte4j.ui.client.Application;
import org.wte4j.ui.client.templates.mapping.MappingDisplay;
import org.wte4j.ui.client.templates.mapping.MappingPresenter;
import org.wte4j.ui.client.templates.upload.TemplateUploadDisplay;
import org.wte4j.ui.client.templates.upload.TemplateUploadPresenter;
import org.wte4j.ui.shared.InvalidTemplateServiceException;
import org.wte4j.ui.shared.TemplateDto;
import org.wte4j.ui.shared.TemplateServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

public class NewTemplateWizard {

	private static final String UPLOAD_URL = Application.REST_SERVICE_BASE_URL + "templates/temp";
	private TemplateServiceAsync templateService = TemplateServiceAsync.Util.getInstance();

	private TemplateUploadDisplay templateUploadDisplay;
	private TemplateUploadPresenter templateUploadPresenter;

	private NewTemplateStepPresenter newTemplatePresenter;
	private NewTemplateDisplay newTemplateDisplay;

	private MappingStepPresenter mappingPresenter;
	private MappingDisplay mappingDisplay;

	private WizardContainer parent;
	private TemplateDto template;
	private String uploadedFile;

	public NewTemplateWizard() {
		super();
		templateUploadPresenter = new TemplateUploadStepPresenter();
		newTemplatePresenter = new NewTemplateStepPresenter();
		mappingPresenter = new MappingStepPresenter();

	}

	public void bind(TemplateUploadDisplay aTemplateUploadDisplay) {
		templateUploadPresenter.bindTo(aTemplateUploadDisplay);
		this.templateUploadDisplay = aTemplateUploadDisplay;
	}

	public void bind(NewTemplateDisplay aNewTemplateDisplay) {
		newTemplatePresenter.bind(aNewTemplateDisplay);
		this.newTemplateDisplay = aNewTemplateDisplay;
	}

	public void bind(MappingDisplay aMappingDisplay) {
		mappingPresenter.bind(aMappingDisplay);
		this.mappingDisplay = aMappingDisplay;
	}

	public void startWihtFileUpload(WizardContainer aContainer) {
		parent = aContainer;
		initForNewTemplate();
		startFileUpload();
		parent.onWizardStarted();
	}

	public void startWithModel(WizardContainer aContainer) {
		parent = aContainer;
		initForNewTemplate();
		startNameAndModelSelection();
		parent.onWizardStarted();
	}

	protected void startFileUpload() {
		templateUploadPresenter.startUpload(template, UPLOAD_URL);
		parent.displayWizardContent(templateUploadDisplay);

	}

	private void initForNewTemplate() {
		template = new TemplateDto();
		template.setDocumentName("new");
		template.setLanguage("en");
		uploadedFile = null;
	}

	protected void onFileUploadCompleted(String fileUpdateResponse) {
		uploadedFile = fileUpdateResponse;
		startNameAndModelSelection();
	}

	protected void startNameAndModelSelection() {
		newTemplatePresenter.present(template);
		parent.displayWizardContent(newTemplateDisplay);
	}

	protected void onNameAndModelSelectionComplete() {
		if (uploadedFile == null || uploadedFile.isEmpty()) {
			persistTemplate(new AsyncCallback<TemplateDto>() {
				@Override
				public void onFailure(Throwable caught) {
					parent.showErrorMessage(caught.getMessage());
					startNameAndModelSelection();

				}

				@Override
				public void onSuccess(TemplateDto newTemplate) {
					onTemplateCreated(newTemplate);

				}
			});
		}
		else {
			startMapping();
		}
	}

	public void persistTemplate(AsyncCallback<TemplateDto> callback) {
		templateService.createTemplate(template, uploadedFile, callback);

	}

	protected void startMapping() {
		mappingPresenter.startEditMapping(template, uploadedFile);
		parent.displayWizardContent(mappingDisplay);
	}

	protected void onTemplateCreated(TemplateDto created) {
		parent.onWizardCompleted(created);
	}

	protected void onWizardCanceled() {
		parent.onWizardCanceled();
		parent = null;
	}

	private class TemplateUploadStepPresenter extends TemplateUploadPresenter {
		public TemplateUploadStepPresenter() {
			this.addFileUploadedHandler(new FileUploadedHandler() {
				@Override
				public void onSuccess(String fileUpdateResponse) {
					onFileUploadCompleted(fileUpdateResponse);
				}

				@Override
				public void onFailure(String errorMessage) {
					parent.showErrorMessage(errorMessage);
				}
			});
		}

		@Override
		protected void cancelFileUpload() {
			super.cancelFileUpload();
			onWizardCanceled();
		}

	}

	private class NewTemplateStepPresenter extends NewTemplatePresenter {

		@Override
		void onSubmitClicked() {
			onNameAndModelSelectionComplete();
		}

		@Override
		void onCancelClicked() {
			onWizardCanceled();
		}

		@Override
		void onFailedLoadDataModels(Throwable cause) {
			parent.showErrorMessage(cause.getMessage());
			onWizardCanceled();
		}

	}

	private class MappingStepPresenter extends MappingPresenter {

		@Override
		protected void submit() {
			persistTemplate(new AsyncCallback<TemplateDto>() {

				@Override
				public void onSuccess(TemplateDto updated) {
					template = updated;
					onSubmitSuccess(updated);
					onTemplateCreated(updated);
				}

				@Override
				public void onFailure(Throwable caught) {
					if (caught instanceof InvalidTemplateServiceException) {
						displayInvalidTemplateMessage((InvalidTemplateServiceException) caught);
					}
					else {
						parent.showErrorMessage(caught.getMessage());
					}

				}
			});

		}

		@Override
		protected void onInitMappingDataFailed(Throwable caught) {
			parent.showErrorMessage(caught.getMessage());
			cancelEditMapping();
		}

		@Override
		public void cancelEditMapping() {
			super.cancelEditMapping();
			onWizardCanceled();
		}
	}

	public interface WizardContainer {
		public void displayWizardContent(IsWidget wiget);

		public void showErrorMessage(String message);

		public void onWizardStarted();

		public void onWizardCanceled();

		public void onWizardCompleted(TemplateDto created);
	}

}
