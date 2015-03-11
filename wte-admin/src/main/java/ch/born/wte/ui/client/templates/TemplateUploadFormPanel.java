package ch.born.wte.ui.client.templates;

import static ch.born.wte.ui.client.Application.LABELS;
import ch.born.wte.ui.shared.TemplateDto;

import com.google.gwt.event.dom.client.ClickEvent;
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

public class TemplateUploadFormPanel extends FormPanel implements
		TemplateUploadDisplay {

	private TemplateUploadPresenter templateUploadPresenter;

	private TemplateDto currentTemplate;
	private String templateUploadRestURL;

	private SubmitButton submitButton;
	private Button cancelButton;

	public TemplateUploadFormPanel() {
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

	private void dataChanged() {
		clear();
		initForm();
		initHandlersEventsDelegation();
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

	private void initHandlersEventsDelegation() {
		submitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (templateUploadPresenter != null)
					templateUploadPresenter.onSubmitButtonClick(clickEvent);
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (templateUploadPresenter != null)
					templateUploadPresenter.onCancelButtonClick(clickEvent);
			}
		});

		addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent submitCompleteEvent) {
				if (templateUploadPresenter != null)
					templateUploadPresenter
							.onSubmitComplete(submitCompleteEvent);
			}
		});
	}

	private void setMeta() {
		setAction(templateUploadRestURL);
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
