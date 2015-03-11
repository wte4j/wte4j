package ch.born.wte.ui.client.templates;

import java.util.ArrayList;
import java.util.List;

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
		FileUploadResponse fileUploadResponse = parseResponse(submitCompleteEvent
				.getResults());
		if (isSubmissionSuccess(fileUploadResponse)) {
			fireSuccessfulFileUpload(fileUploadResponse.getResponse());
		} else {
			fireFailedFileUpload(fileUploadResponse.getError());
		}
	}

	private void initHandlers() {
		submitClickHandlers = new ArrayList<ClickHandler>();
		cancelClickHandlers = new ArrayList<ClickHandler>();
		fileUploadedHandlers = new ArrayList<FileUploadedHandler>();
	}

	private FileUploadResponse parseResponse(String results) {
		FileUploadResponse fileUploadResponse = null;
		if (results != null
				&& JsonUtils.safeToEval(results.replaceAll("\\<[^>]*>", ""))) {
			fileUploadResponse = JsonUtils.safeEval(results.replaceAll(
					"\\<[^>]*>", ""));
		} else {
			fileUploadResponse = JsonUtils
					.safeEval("{\"error\":\"wte.message.fileupload.err.format\"}");
		}
		return fileUploadResponse;
	}

	protected boolean isSubmissionSuccess(FileUploadResponse fileUploadResponse) {
		boolean isSuccess = (fileUploadResponse != null);
		if (isSuccess) {
			isSuccess = isSuccess && (fileUploadResponse.getError() == null);
		}
		return isSuccess;
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

	public static class FileUploadResponse extends JavaScriptObject {
		protected FileUploadResponse() {
		}

		public final native String getError()/*-{
			return this.error;
		}-*/;

		public final native String getResponse()/*-{
			return this.response;
		}-*/;
	}
}
