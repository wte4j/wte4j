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
package org.wte4j.examples.showcase.client.management;

import java.util.HashMap;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.gwt.HTMLPanel;
import org.wte4j.ui.client.templates.TemplateListDisplay;
import org.wte4j.ui.client.templates.TemplateListPanel;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class TemplateManagerPanel extends Composite implements TemplateManagerDisplay {

	private static TemplateManagerPanelUiBinder uiBinder = GWT.create(TemplateManagerPanelUiBinder.class);

	@UiField
	Modal dialog;

	@UiField
	ModalBody dialogBody;

	@UiField
	Button addTemplate;

	@UiField
	Button addTemplateFromFile;

	@UiField
	HTMLPanel templateList;

	private Map<String, LinkedGroupItem> dataModelNameToItem = new HashMap<String, LinkedGroupItem>();

	private TemplateListDisplay templateListDisplay;

	public TemplateManagerPanel() {
		this.initWidget(uiBinder.createAndBindUi(this));
		initModal();
		initTemplateList();
	}

	private void initTemplateList() {
		templateListDisplay = new TemplateListPanel();
		templateList.clear();
		templateList.add(templateListDisplay);
	}

	private void initModal() {
		dialog.setClosable(false);
		dialog.setDataBackdrop(ModalBackdrop.STATIC);
	}

	@Override
	public void showAddTemplateDialog() {
		dialog.show();
	}

	@Override
	public void hideAddTemplateDialog() {
		dialog.hide();
	}

	@Override
	public void addAddTemplateClickHandler(ClickHandler clickHandler) {
		addTemplate.addClickHandler(clickHandler);
	}

	@Override
	public void addCreateTemplateFromFile(ClickHandler clickHandler) {
		addTemplateFromFile.addClickHandler(clickHandler);

	}

	@Override
	public TemplateListDisplay getTemplateListDisplay() {
		return templateListDisplay;
	}

	@Override
	public void setDialogContent(IsWidget content, String title) {
		dialog.setTitle(title);
		dialogBody.clear();
		dialogBody.add(content);
	}

	interface TemplateManagerPanelUiBinder extends UiBinder<Widget, TemplateManagerPanel> {
	}
}
