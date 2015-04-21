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
package org.wte4j.examples.showcase.client.templatelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.form.error.BasicEditorError;
import org.gwtbootstrap3.client.ui.form.validator.Validator;
import org.gwtbootstrap3.client.ui.gwt.HTMLPanel;
import org.wte4j.examples.showcase.client.Application;
import org.wte4j.ui.client.templates.TemplateListPanel;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TemplateAdminListPanel extends Composite implements TemplateAdminListDisplay {

	private static TemplateAdminListPanelUiBinder uiBinder = GWT.create(TemplateAdminListPanelUiBinder.class);

	@UiField
	Modal dialog;

	@UiField
	Button addTemplate;

	@UiField
	HTMLPanel templateList;
	
	@UiField
	TextBox templateName;
	
	@UiField
	LinkedGroup dataModelList;

	@UiField
	Form createTemplateForm;
	
	@UiField
	Button closeDialog;
	
	@UiField
	Button createTemplate;

	@UiField
	FormGroup alertBox;
	
	@UiField
	Image loadingSpinner;

	private Map<DataModelItem, LinkedGroupItem> dataModelNameToItem = new HashMap<DataModelItem, LinkedGroupItem>();

	public TemplateAdminListPanel() {
		this.initWidget(uiBinder.createAndBindUi(this));
		initTemplateName();
		initModal();
		initSpinner();
	}

	@SuppressWarnings("unchecked")
	private void initTemplateName() {
		Validator<String> validator = new Validator<String>() {
			@Override
			public int getPriority() {
				return 0;
			}

			@Override
			public List<EditorError> validate(Editor<String> editor,
					String value) {
				List<EditorError> errors = new ArrayList<EditorError>();
				if (value == null || "".equals(value)) {
					errors.add(new BasicEditorError(editor, value, Application.MESSAGES.wte4j_message_template_name_not_empty()));
				}
				return errors;
			}
		};
		templateName.setValidators(validator);
		templateName.setValidateOnBlur(true);
	}
	
	private void initModal() {
		dialog.setClosable(false);
		dialog.setDataBackdrop(ModalBackdrop.STATIC);
	}
	
	private void initSpinner() {
		loadingSpinner.setResource(Application.RESOURCES.loadingSpinner());
		loadingSpinner.setVisible(false);
	}

	@Override
	public void setDataModelListItems(List<DataModelItem> dataModelItems) {
		dataModelList.clear();
		boolean first = true;
		for (DataModelItem item : dataModelItems) {
			LinkedGroupItem groupItem = new LinkedGroupItem();
			groupItem.setText(item.getText());
			groupItem.addClickHandler(item.getClickHandler());
			if (first) {
				item.setActive(true);
			} else {
				item.setActive(false);
			}
			first = false;
			dataModelNameToItem.put(item, groupItem);
			dataModelList.add(groupItem);
		}
		activeDataModelItemChanged();
	}

	@Override
	public void showDataModelList() {
		templateName.clear();
		dialog.show();
	}

	@Override
	public void hideDataModelList() {
		dialog.hide();
	}
	
	@Override
	public String getTemplateName() {
		return templateName.getText();
	}

	@Override
	public void addAddTemplateClickHandler(ClickHandler clickHandler) {
		addTemplate.addClickHandler(clickHandler);;
	}
	
	@Override
	public void addCloseDialogClickHandler(ClickHandler clickHandler) {
		closeDialog.addClickHandler(clickHandler);
	}
	
	@Override
	public void addCreateTemplateClickHandler(ClickHandler clickHandler) {
		createTemplate.addClickHandler(clickHandler);
	}
	
	@Override
	public boolean validate() {
		return createTemplateForm.validate() && isOneDataModelActive();
	}

	private boolean isOneDataModelActive() {
		boolean oneDataModel = false;
		for (DataModelItem item : dataModelNameToItem.keySet()) {
			if (!oneDataModel && item.isActive()) {
				oneDataModel = true;
			} else if (oneDataModel && item.isActive()) {
				oneDataModel = false;
				break;
			}
		}
		if (!oneDataModel)
			displayError(Application.MESSAGES.wte4j_message_one_data_model_selected());
		return oneDataModel;
	}

	@Override
	public void activeDataModelItemChanged() {
		for (DataModelItem item : dataModelNameToItem.keySet()) {
			dataModelNameToItem.get(item).setActive(item.isActive());
		}
	}
	
	@Override
	public String getActiveDataModel() {
		for (DataModelItem item : dataModelNameToItem.keySet()) {
			if (item.isActive())
				return item.getText();
		}
		return "";
	}
	
	@Override
	public void removeActiveDataModel() {
		for (DataModelItem item : dataModelNameToItem.keySet()) {
			if (item.isActive())
				item.setActive(false);
		}
	}
	
	@Override
	public void showModalLoading() {
		closeDialog.setVisible(false);
		createTemplate.setVisible(false);
		loadingSpinner.setVisible(true);
	}
	
	@Override
	public void hideModalLoading() {
		closeDialog.setVisible(true);
		createTemplate.setVisible(true);
		loadingSpinner.setVisible(false);
	}
	
	@Override
	public void displayError(String message) {
		Alert infoAlert = new Alert(message, AlertType.DANGER);
		infoAlert.setDismissable(true);
		alertBox.add(infoAlert);
	}
	
	@Override
	public void setTemplateListPanel(TemplateListPanel displayTemplatesPanel) {
		templateList.clear();
		templateList.add(displayTemplatesPanel);
	}

	interface TemplateAdminListPanelUiBinder extends UiBinder<Widget, TemplateAdminListPanel> {
	}
}
