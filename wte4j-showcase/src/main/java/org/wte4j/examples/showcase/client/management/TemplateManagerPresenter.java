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

import java.util.ArrayList;
import java.util.List;

import org.wte4j.examples.showcase.client.Application;
import org.wte4j.examples.showcase.client.GrowlFactory;
import org.wte4j.examples.showcase.shared.service.TemplateManagerServiceAsync;
import org.wte4j.ui.client.templates.TemplateListDisplay;
import org.wte4j.ui.client.templates.TemplateListPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TemplateManagerPresenter {

	private TemplateManagerServiceAsync templateService = TemplateManagerServiceAsync.Util
			.getInstance();

	private TemplateManagerDisplay display;

	private TemplateListPresenter displayTemplatesPresenter;

	public void bind(TemplateManagerDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException("presenter is already bound");
		}
		display = aDisplay;
		bindTemplateList();
		initAddTemplateButton();
		initDialogButtons();
	}

	private void initDialogButtons() {
		display.addCloseDialogClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				display.hideDataModelList();
			}
		});

		display.addCreateTemplateClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				createTemplate();
			}
		});
	}

	private void bindTemplateList() {
		TemplateListDisplay displayTemplatesDisplay = display.getTemplateListDisplay();
		displayTemplatesPresenter = new TemplateListPresenter();
		displayTemplatesPresenter.bindTo(displayTemplatesDisplay);
		displayTemplatesPresenter.loadData();
	}

	private void initAddTemplateButton() {
		display.addAddTemplateClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				displayDataModelListForSelection();
			}
		});
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

		private String dataModel;

		public DataModelClickHandler(String dataModel) {
			this.dataModel = dataModel;
		}

		@Override
		public void onClick(ClickEvent event) {
			display.setSelectedDataModel(dataModel);
		}
	}

	private void createTemplate() {
		if (display.validate()) {
			display.showSpinner();
			final String dataModel = display.getSelectedDataModel();
			final String templateName = display.getTemplateName();
			createTemplate(dataModel, templateName);
		}
	}

	private void createTemplate(String dataModel, final String templateName) {
		templateService.createTemplate(dataModel,
				templateName, new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void nothing) {
						display.hideSpinner();
						display.hideDataModelList();
						GrowlFactory.success(Application.MESSAGES.wte4j_message_template_creation_success(templateName));
						displayTemplatesPresenter.loadData();
					}

					@Override
					public void onFailure(Throwable e) {
						display.hideSpinner();
						display.displayError(e.getMessage());
					}
				});
	}

}
