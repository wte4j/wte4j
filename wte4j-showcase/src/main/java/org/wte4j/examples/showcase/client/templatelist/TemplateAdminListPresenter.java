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
import java.util.List;

import org.gwtbootstrap3.client.ui.gwt.HTMLPanel;
import org.wte4j.examples.showcase.shared.service.TemplateServiceAsync;
import org.wte4j.ui.client.templates.TemplateListPanel;
import org.wte4j.ui.client.templates.TemplateListPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TemplateAdminListPresenter {

	private TemplateServiceAsync templateService = TemplateServiceAsync.Util.getInstance();

	private TemplateAdminListDisplay display;

	private String selectedDataModel;

	private TemplateListPresenter displayTemplatesPresenter;

	public void bind(TemplateAdminListDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException("presenter is allready bound");
		}
		display = aDisplay;
		bindTemplateList();
		initAddTemplateButton();
	}

	private void bindTemplateList() {
		HTMLPanel panel = display.getTemplateListPanel();
		TemplateListPanel displayTemplatesPanel = new TemplateListPanel();	
		displayTemplatesPresenter = new TemplateListPresenter();
		displayTemplatesPresenter.bindTo(displayTemplatesPanel);
		panel.add(displayTemplatesPanel);
		displayTemplatesPresenter.loadData();
	}

	private void initAddTemplateButton() {
		display.getAddTemplateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				displayDataModelListForSelection();
			}
		});
		display.getAddTemplateButton().setText("Add Template");
	}

	public void displayDataModelListForSelection() {
		templateService.listDataModel(new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> dataModels) {
				List<DataModelItem> dataModelItems = new ArrayList<DataModelItem>();
				for (String dataModel : dataModels) {
					dataModelItems.add(createDataModelItem(dataModel));
				}
				display.setDataModelListItems(dataModelItems);
				display.showDataModelList();

			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		});

	}

	DataModelItem createDataModelItem(String dataModel) {
		DataModelItem dataModelItem = new DataModelItem();
		dataModelItem.setText(dataModel);
		dataModelItem.setClickHandler(new DataModelClickHandler(dataModel));
		return dataModelItem;
	}

	private class DataModelClickHandler implements ClickHandler {

		private String className;

		public DataModelClickHandler(String className) {
			this.className = className;
		}

		@Override
		public void onClick(ClickEvent event) {
			selectedDataModel = className;
			createTemplate();
		}

	}
	private void createTemplate() {
		templateService.createTemplate(selectedDataModel, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				display.hideDataModelList();
				displayTemplatesPresenter.loadData();
			}
			@Override
			public void onFailure(Throwable arg0) {
			}
		});
	}

}
