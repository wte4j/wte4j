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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.InlineHelpBlock;
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.form.error.BasicEditorError;
import org.gwtbootstrap3.client.ui.form.validator.Validator;
import org.wte4j.examples.showcase.client.Application;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NewTemplatePanel extends Composite implements NewTemplateDisplay {

	private static NewTemplatePanelUiBinder uiBinder = GWT.create(NewTemplatePanelUiBinder.class);

	@UiField
	Form createTemplateForm;

	@UiField
	TextBox templateName;

	@UiField
	InlineHelpBlock dataModelAlert;

	@UiField
	LinkedGroup dataModelList;

	@UiField
	Button createTemplate;

	@UiField
	Button closeDialog;

	@UiField
	Image loadingSpinner;

	private Map<String, LinkedGroupItem> dataModelNameToItem = new HashMap<String, LinkedGroupItem>();

	public NewTemplatePanel() {
		this.initWidget(uiBinder.createAndBindUi(this));
		initTemplateName();
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

	private void initSpinner() {
		loadingSpinner.setResource(Application.RESOURCES.loadingSpinner());
		loadingSpinner.setVisible(false);
	}

	@Override
	public void setDataModelListItems(List<DataModelItem> dataModelItems) {
		dataModelList.clear();
		dataModelNameToItem.clear();
		boolean first = true;
		for (DataModelItem item : dataModelItems) {
			LinkedGroupItem groupItem = new LinkedGroupItem();
			groupItem.setText(item.getText());
			groupItem.addClickHandler(item.getClickHandler());
			if (first) {
				groupItem.setActive(true);
			} else {
				groupItem.setActive(false);
			}
			first = false;
			dataModelNameToItem.put(item.getText(), groupItem);
			dataModelList.add(groupItem);
		}
	}

	@Override
	public String getTemplateName() {
		return templateName.getText();
	}

	@Override
	public void setTemplateName(String value) {
		templateName.setValue(value);
	}

	@Override
	public void setFocusOnTemplateName() {
		templateName.selectAll();
		templateName.setFocus(true);

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
	public boolean validateAddTemplateForm() {
		return createTemplateForm.validate() && isOneDataModelActive();
	}

	private boolean isOneDataModelActive() {
		boolean oneDataModel = false;
		for (String dataModel : dataModelNameToItem.keySet()) {
			LinkedGroupItem item = dataModelNameToItem.get(dataModel);
			if (!oneDataModel && item.isActive()) {
				oneDataModel = true;
			} else if (oneDataModel && item.isActive()) {
				oneDataModel = false;
				break;
			}
		}
		if (!oneDataModel) {
			dataModelAlert.setText(Application.MESSAGES.wte4j_message_one_data_model_selected());
			dataModelAlert.setVisible(true);
		} else {
			dataModelAlert.setVisible(false);
		}
		return oneDataModel;
	}

	@Override
	public String getSelectedDataModel() {
		for (String dataModel : dataModelNameToItem.keySet()) {
			LinkedGroupItem item = dataModelNameToItem.get(dataModel);
			if (item.isActive())
				return dataModel;
		}
		return "";
	}

	@Override
	public void setSelectedDataModel(String selectedDataModel) {
		for (String dataModel : dataModelNameToItem.keySet()) {
			LinkedGroupItem item = dataModelNameToItem.get(dataModel);
			if (dataModel.equals(selectedDataModel))
				item.setActive(true);
			else
				item.setActive(false);
		}
	}

	@Override
	public void showSpinner() {
		closeDialog.setVisible(false);
		createTemplate.setVisible(false);
		loadingSpinner.setVisible(true);
	}

	@Override
	public void hideSpinner() {
		closeDialog.setVisible(true);
		createTemplate.setVisible(true);
		loadingSpinner.setVisible(false);
	}

	interface NewTemplatePanelUiBinder extends UiBinder<Widget, NewTemplatePanel> {
	}
}
