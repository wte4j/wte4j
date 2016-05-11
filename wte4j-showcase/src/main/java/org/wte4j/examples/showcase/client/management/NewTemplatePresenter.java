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
import java.util.List;

import org.wte4j.examples.showcase.shared.service.OrderServiceAsync;
import org.wte4j.ui.shared.TemplateDto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class NewTemplatePresenter {
	private OrderServiceAsync orderService = OrderServiceAsync.Util.getInstance();
	private NewTemplateDisplay display;

	private TemplateDto template;

	public void bind(NewTemplateDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException("presenter is already bound");
		}
		display = aDisplay;
		initDialogButtons();
		loadDataModels();
	}

	private void initDialogButtons() {
		display.addCreateTemplateClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				validateAndSubmitTemplate();
			}
		});

		display.addCloseDialogClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onCancelClicked();
			}
		});
	}

	public void present(TemplateDto aTemplate) {
		template = aTemplate;
		display.setTemplateName(template.getDocumentName());
		display.setSelectedDataModel(template.getInputType());
		display.hideSpinner();
		display.setFocusOnTemplateName();
		loadDataModels();
	}

	protected void loadDataModels() {
		orderService.listDataModel(new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> dataModels) {
				onSuccessLoadDataModels(dataModels);
			}

			@Override
			public void onFailure(Throwable caught) {
				onFailedLoadDataModels(caught);
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

	abstract void onFailedLoadDataModels(Throwable cause);

	protected void onSuccessLoadDataModels(List<String> dataModels) {
		List<DataModelItem> dataModelItems = new ArrayList<DataModelItem>();
		for (String dataModel : dataModels) {
			dataModelItems.add(createDataModelItem(dataModel));
		}
		display.setDataModelListItems(dataModelItems);
		display.setFocusOnTemplateName();
	}

	public void validateAndSubmitTemplate() {
		if (display.validateAddTemplateForm()) {
			display.showSpinner();

			final String dataModel = display.getSelectedDataModel();
			template.setInputType(dataModel);

			final String templateName = display.getTemplateName();
			template.setDocumentName(templateName);

			onSubmitClicked();
		}
	}

	abstract void onSubmitClicked();

	abstract void onCancelClicked();

}
