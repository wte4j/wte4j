package ch.born.wte.ui.client.templates;

import static ch.born.wte.ui.client.Application.LABELS;
import ch.born.wte.ui.shared.TemplateDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;

public class TemplateUploadFormPanel extends Composite implements
		TemplateUploadDisplay {

	@UiField
	FormPanel formPanel;

	@UiField
	Label formTitle;

	@UiField
	SubmitButton submitButton;
	
	@UiField
	Button cancelButton;
	
	@UiField
	FileUpload fileUpload;
	
	@UiField
	Hidden templateName;
	
	@UiField
	Hidden templateLanguage;
	
	
	private static TemplateUploadFormPanelUiBInder uiBinder = GWT
			.create(TemplateUploadFormPanelUiBInder.class);
	
	private TemplateUploadPresenter templateUploadPresenter;
	private TemplateDto currentTemplate;
	private String templateUploadRestURL;

	public TemplateUploadFormPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		initButtons();
	}

	@Override
	public void setPresenter(TemplateUploadPresenter templateUploadPresenter) {
		this.templateUploadPresenter = templateUploadPresenter;
	}

	@Override
	public void setData(TemplateDto currentTemplate,
			String templateUploadRestURL) {
		this.currentTemplate = currentTemplate;
		this.templateUploadRestURL = templateUploadRestURL;
		dataChanged();
	}
	
	@UiHandler("submitButton")
	void onSubmitButtonClicked(ClickEvent clickEvent) {
		if (templateUploadPresenter != null)
			templateUploadPresenter.onSubmitButtonClick(clickEvent);
	}
	
	@UiHandler("cancelButton")
	void onCancelButtonClicked(ClickEvent clickEvent) {
		if (templateUploadPresenter != null)
			templateUploadPresenter.onCancelButtonClick(clickEvent);
	}
	
	@UiHandler("formPanel")
	void onSubmitComplete(SubmitCompleteEvent submitCompleteEvent) {
		if (templateUploadPresenter != null)
			templateUploadPresenter.onSubmitComplete(submitCompleteEvent);
	}

	private void initButtons() {
		submitButton.setText(LABELS.submit());
		cancelButton.setText(LABELS.cancel());
	}

	private void dataChanged() {
		setMetaData();
		setVisibleContent();
		setHiddenContent();
	}

	private void setMetaData() {
		formPanel.setAction(templateUploadRestURL);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
	}

	private void setVisibleContent() {
		setFormTitle();
		setFileUploadInput();
	}

	private void setHiddenContent() {
		setNameTextBox();
		setLanguageTextBox();
	}

	private void setFormTitle() {
		formTitle.setText(LABELS.updateTemplate());
	}

	private void setFileUploadInput() {
		fileUpload.setName("file");
	}

	private void setNameTextBox() {
		templateName.setName("name");
		templateName.setValue(currentTemplate.getDocumentName());
	}

	private void setLanguageTextBox() {
		templateLanguage.setName("language");
		templateLanguage.setValue(currentTemplate.getLanguage());
	}
	
	interface TemplateUploadFormPanelUiBInder extends
			UiBinder<Widget, TemplateUploadFormPanel> {
	}
}
