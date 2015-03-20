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
package ch.born.wte.ui.client.templates.upload;

import java.util.ArrayList;
import java.util.List;

import ch.born.wte.ui.shared.FileUploadResponse;
import ch.born.wte.ui.shared.FileUploadResponseDto;
import ch.born.wte.ui.shared.TemplateDto;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

public class TemplateUploadPresenter {
	private TemplateUploadDisplay templateUploadDisplay;
	private List<FileUploadedHandler> fileUploadedHandlers;
	private List<ClickHandler> submitClickHandlers;
	private List<ClickHandler> cancelClickHandlers;
	private TemplateDto currentTemplate;
	private String templateUploadRestURL;

	public TemplateUploadPresenter(TemplateDto currentTemplate,
			String templateUploadRestURL) {
		this.currentTemplate = currentTemplate;
		this.templateUploadRestURL = templateUploadRestURL;
		initHandlers();
	}

	public void addSubmitButtonClickHandler(ClickHandler clickHandler) {
		submitClickHandlers.add(clickHandler);
	}

	public void addCancelButtonClickHandler(ClickHandler clickHandler) {
		cancelClickHandlers.add(clickHandler);
	}

	public void addFileUploadedHandler(FileUploadedHandler fileUploadedHandler) {
		fileUploadedHandlers.add(fileUploadedHandler);
	}

	public void bindTo(TemplateUploadDisplay templateUploadDisplay) {
		this.templateUploadDisplay = templateUploadDisplay;
		this.templateUploadDisplay.setData(currentTemplate,
				templateUploadRestURL);
		this.templateUploadDisplay.setPresenter(this);
	}

	public void onSubmitButtonClick(ClickEvent clickEvent) {
		for (ClickHandler clickHandler : submitClickHandlers)
			clickHandler.onClick(clickEvent);
	}

	public void onCancelButtonClick(ClickEvent clickEvent) {
		for (ClickHandler clickHandler : cancelClickHandlers)
			clickHandler.onClick(clickEvent);
	}

	public void onSubmitComplete(SubmitCompleteEvent submitCompleteEvent) {
		FileUploadResponse response = parseResponse(submitCompleteEvent.getResults());
		if (response.getDone()) {
			fireSuccessfulFileUpload(response.getMessage());
		}
		else {
			fireFailedFileUpload(response.getMessage());
		}
	}

	private void initHandlers() {
		submitClickHandlers = new ArrayList<ClickHandler>();
		cancelClickHandlers = new ArrayList<ClickHandler>();
		fileUploadedHandlers = new ArrayList<FileUploadedHandler>();
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

	public static class FileUploadResponseJso extends JavaScriptObject implements FileUploadResponse {
		protected FileUploadResponseJso() {
		}

		public final native boolean getDone()/*-{
			return this.done;
		}-*/;

		public final native String getMessage() /*-{
			return this.message;
		}-*/;

	}
}
