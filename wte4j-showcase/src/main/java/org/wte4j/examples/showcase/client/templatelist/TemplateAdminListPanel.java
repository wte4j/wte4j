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

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.gwt.HTMLPanel;

import com.google.gwt.core.shared.GWT;
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
	LinkedGroup dataModelList;

	public TemplateAdminListPanel() {
		this.initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setDataModelListItems(List<DataModelItem> dataModelItems) {
		dataModelList.clear();
		for (DataModelItem item : dataModelItems) {
			LinkedGroupItem groupItem = new LinkedGroupItem();
			groupItem.setText(item.getText());
			groupItem.addClickHandler(item.getClickHandler());
			dataModelList.add(groupItem);
		}
	}

	@Override
	public void showDataModelList() {
		dialog.show();
	}

	@Override
	public void hideDataModelList() {
		dialog.hide();
	}
	
	@Override
	public Button getAddTemplateButton() {
		return addTemplate;
	}
	
	@Override
	public HTMLPanel getTemplateListPanel() {
		return templateList;
	}

	interface TemplateAdminListPanelUiBinder extends UiBinder<Widget, TemplateAdminListPanel> {
	}

}
