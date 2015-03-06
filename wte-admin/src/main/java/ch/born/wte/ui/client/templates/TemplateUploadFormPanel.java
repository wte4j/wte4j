package ch.born.wte.ui.client.templates;

import static ch.born.wte.ui.client.Application.LABELS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.born.wte.ui.shared.TemplateDto;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TemplateUploadFormPanel extends FormPanel {
	TemplateDto currentTemplate;
	String templateFileRestURL;

	private SubmitButton submitButton;
	private Button cancelButton;
	private List<FileUploadedHandler> fileUploadedHandlers;

	public TemplateUploadFormPanel(TemplateDto currentTemplate,
			String templateFileRestURL) {
		this.currentTemplate = currentTemplate;
		this.templateFileRestURL = templateFileRestURL;
		initHandlers();
		initButtons();
		initForm();
	}

	public void addSubmitButtonClickHandler(ClickHandler clickHandler) {
		submitButton.addClickHandler(clickHandler);
	}

	public void addCancelButtonClickHandler(ClickHandler clickHandler) {
		cancelButton.addClickHandler(clickHandler);
	}

	public void addFileUploadedHandler(FileUploadedHandler fileUploadedHandler) {
		fileUploadedHandlers.add(fileUploadedHandler);
	}

	public interface FileUploadedHandler {
		public void onSuccess(String fileUpdateResponse);

		public void onFailure(String errorMessage);
	}

	private void initHandlers() {
		fileUploadedHandlers = new ArrayList<FileUploadedHandler>();
		addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent submitCompleteEvent) {
				FileUploadResponse fileUploadResponse = parseResponse(submitCompleteEvent.getResults());
				if (isSubmissionSuccess(fileUploadResponse)) {
					fireSuccessfulFileUpload(fileUploadResponse.getResponse());
				} else {
					fireFailedFileUpload(fileUploadResponse.getError());
				}
			}
		});
	}

	protected FileUploadResponse parseResponse(String results) {
		FileUploadResponse fileUploadResponse = null;
		if (results != null && JsonUtils.safeToEval(results.replaceAll("\\<[^>]*>",""))) {
			fileUploadResponse = JsonUtils.safeEval(results.replaceAll("\\<[^>]*>",""));
		} else {
			fileUploadResponse = JsonUtils.safeEval("{\"error\":\"wte.message.fileupload.err.format\"}");
		}
		return fileUploadResponse;
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

	private void initButtons() {

		submitButton = new SubmitButton(LABELS.submit());
		cancelButton = new SubmitButton(LABELS.cancel());
	}

	private void initForm() {
		setMeta();
		VerticalPanel contentPanel = new VerticalPanel();
		contentPanel.add(getVisibleContent());
		contentPanel.add(getButtonsPanel());
		contentPanel.add(getHiddenContent());
		add(contentPanel);
	}

	private void setMeta() {
		setAction(templateFileRestURL);
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);
	}

	private Widget getVisibleContent() {
		VerticalPanel formVPanel = new VerticalPanel();
		formVPanel.setSpacing(10);
		formVPanel.add(getFormTitle());
		formVPanel.add(getFileUploadInput());
		return formVPanel;
	}

	private Widget getButtonsPanel() {
		HorizontalPanel buttonsHPanel = new HorizontalPanel();
		buttonsHPanel.setSpacing(10);

		buttonsHPanel.add(submitButton);
		buttonsHPanel.add(cancelButton);

		return buttonsHPanel;
	}

	private Widget getHiddenContent() {
		VerticalPanel formVPanel = new VerticalPanel();
		formVPanel.add(getNameTextBox());
		formVPanel.add(getLanguageTextBox());

		return formVPanel;
	}

	private Label getFormTitle() {
		Label formTitle = new Label();
		formTitle.setText(LABELS.updateTemplate());
		return formTitle;
	}

	private FileUpload getFileUploadInput() {
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("file");
		return fileUpload;
	}

	private Widget getNameTextBox() {
		TextBox name = new TextBox();
		name.setName("name");
		name.setValue(currentTemplate.getDocumentName());
		name.setVisible(false);
		return name;
	}

	private Widget getLanguageTextBox() {
		TextBox language = new TextBox();
		language.setName("language");
		language.setValue(currentTemplate.getLanguage());
		language.setVisible(false);
		return language;
	}
}
