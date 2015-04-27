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
package org.wte4j.ui.client.templates.upload;

import static org.wte4j.ui.client.Application.LABELS;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.gwt.FormPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;

public class TemplateUploadFormPanel extends Composite implements
		TemplateUploadDisplay {

	@UiField(provided = true)
	FormPanel formPanel;
	
	@UiField
	FlowPanel flowPanel;

	@UiField
	Button submitButton;

	@UiField
	Button cancelButton;

	@UiField
	FileUpload fileUpload;

	@UiField
	Hidden templateName;

	@UiField
	Hidden templateLanguage;

	@UiField
	Image loadingSpinner;

	private static TemplateUploadFormPanelUiBInder uiBinder = GWT
			.create(TemplateUploadFormPanelUiBInder.class);

	public TemplateUploadFormPanel() {
		initForm();
		initWidget(uiBinder.createAndBindUi(this));
		initIFrame();
		initButtons();
	}

	private void initForm() {
		formPanel = new FormPanel("wte4j-fileupload-iframe");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
	}

	private void initIFrame() {
		final Frame iframe = new Frame("javascript:\"\"");
		iframe.getElement().setAttribute("name", "wte4j-fileupload-iframe");
		iframe.getElement().setAttribute("style","position:absolute;width:0;height:0;border:0");
		iframe.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent arg0) {
				String content = IFrameElement.as(iframe.getElement()).getContentDocument().getBody().getInnerHTML();
				if (content != null && !"".equals(content))
					formPanel.fireEvent(new SubmitCompleteEvent(content){});
			}
		});
		flowPanel.add(iframe);
	}

	@Override
	public void setAction(String templateUploadRestURL) {
		formPanel.setAction(templateUploadRestURL);
	}

	@Override
	public void showSpinner() {
		loadingSpinner.setVisible(true);
		setButtonsVisible(false);
	}
	
	@Override
	public void hideSpinner() {
		loadingSpinner.setVisible(false);
		setButtonsVisible(true);
	}
	
	@Override
	public void addSubmitButtonClickHandler(ClickHandler clickHandler) {
		submitButton.addClickHandler(clickHandler);

	}

	@Override
	public void addCancelButtonClickHandler(ClickHandler clickHandler) {
		cancelButton.addClickHandler(clickHandler);
	}

	@Override
	public void addSubmitCompleteHandler(SubmitCompleteHandler handler) {
		formPanel.addSubmitCompleteHandler(handler);
	}
	
	@Override
	public void submitForm() {
		formPanel.submit();
	}
	
	private void setButtonsVisible(boolean visible) {
		submitButton.setVisible(visible);
		cancelButton.setVisible(visible);
	}
	
	private void initButtons() {
		submitButton.setText(LABELS.submit());
		cancelButton.setText(LABELS.cancel());
	}

	public void setTemplateName(String value) {
		templateName.setValue(value);
	}

	public void setLanguage(String value) {
		templateLanguage.setValue(value);
	}

	interface TemplateUploadFormPanelUiBInder extends
			UiBinder<Widget, TemplateUploadFormPanel> {
	}


}
