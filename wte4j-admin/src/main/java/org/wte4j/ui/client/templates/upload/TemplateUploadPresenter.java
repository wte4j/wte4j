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
package org.wte4j.ui.client.templates.upload;

import java.util.ArrayList;
import java.util.List;

import org.wte4j.ui.shared.FileUploadResponse;
import org.wte4j.ui.shared.FileUploadResponseDto;
import org.wte4j.ui.shared.TemplateDto;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class TemplateUploadPresenter {
	private TemplateUploadDisplay templateUploadDisplay;
	private List<FileUploadedHandler> fileUploadedHandlers;

	public TemplateUploadPresenter() {
		fileUploadedHandlers = new ArrayList<FileUploadedHandler>();
	}

	public void addFileUploadedHandler(FileUploadedHandler fileUploadedHandler) {
		fileUploadedHandlers.add(fileUploadedHandler);
	}

	public void bindTo(TemplateUploadDisplay aTemplateUploadDisplay) {
		this.templateUploadDisplay = aTemplateUploadDisplay;

		templateUploadDisplay.addSubmitButtonClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				templateUploadDisplay.startLoadingAnimation();
				templateUploadDisplay.submitForm();
			}
		});

		templateUploadDisplay.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				onFileUploaded(event);
			}
		});

		templateUploadDisplay.addCancelButtonClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancelFileUpload();
			}

		});

	}

	protected void cancelFileUpload() {
		templateUploadDisplay.stopLoadingAnimation();
	}

	public void startUpload(TemplateDto currentTemplate,
			String templateUploadRestURL) {
		templateUploadDisplay.setAction(templateUploadRestURL);
		templateUploadDisplay.setTemplateName(currentTemplate.getDocumentName());
		templateUploadDisplay.setLanguage(currentTemplate.getLanguage());
		templateUploadDisplay.stopLoadingAnimation();
	}

	private FileUploadResponse parseResponse(String results) {
		if (results != null) {
			// json is wrapped in <pre/> tag
			String resultConent = results.replaceAll("\\<[^>]*>", "");
			if (JsonUtils.safeToEval(resultConent)) {
				return JsonUtils.<FileUploadResponseJso> safeEval(resultConent);
			}

		}
		return new FileUploadResponseDto(false, results);
	}

	private void onFileUploaded(SubmitCompleteEvent event) {
		templateUploadDisplay.stopLoadingAnimation();
		FileUploadResponse response = parseResponse(event.getResults());
		if (response.getDone()) {
			fireSuccessfulFileUpload(response.getMessage());
		}
		else {
			fireFailedFileUpload(response.getMessage());
		}
	}

	private void fireFailedFileUpload(String errorMessage) {
		for (FileUploadedHandler fileUploadHandler : fileUploadedHandlers) {
			fileUploadHandler.onFailure(errorMessage);
		}
	}

	private void fireSuccessfulFileUpload(String fileUpdateResponse) {
		for (FileUploadedHandler fileUploadHandler : fileUploadedHandlers) {
			fileUploadHandler.onSuccess(fileUpdateResponse);
		}
	}

	public interface FileUploadedHandler {
		public void onSuccess(String fileUpdateResponse);

		public void onFailure(String errorMessage);
	}
}
