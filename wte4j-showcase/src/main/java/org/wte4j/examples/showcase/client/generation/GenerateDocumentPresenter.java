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
package org.wte4j.examples.showcase.client.generation;

import java.util.ArrayList;
import java.util.List;

import org.wte4j.examples.showcase.client.Application;
import org.wte4j.examples.showcase.shared.OrderDataDto;
import org.wte4j.examples.showcase.shared.service.OrderServiceAsync;
import org.wte4j.ui.client.dialog.DialogType;
import org.wte4j.ui.client.dialog.MessageDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

public class GenerateDocumentPresenter {

	private OrderServiceAsync orderService = OrderServiceAsync.Util.getInstance();

	private GenerateDocumentDisplay display;

	private NoSelectionModel<OrderDataDto> orderSelectionModel;
	private OrderDataDto selectedOrder;
	private String selectedTemplate;

	public void bind(GenerateDocumentDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException("presenter is already bound");
		}
		display = aDisplay;
		bindOrderContainer();
		initOrderContainer();
	}

	private void bindOrderContainer() {
		orderSelectionModel = new NoSelectionModel<OrderDataDto>();
		orderSelectionModel.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				displayTemplatesForSelection();
			}

		});
		display.getOrderContainer().setSelectionModel(orderSelectionModel);

	}

	public void initOrderContainer() {
		orderService.listOrderData(new AsyncCallback<List<OrderDataDto>>() {

			@Override
			public void onSuccess(List<OrderDataDto> list) {
				display.getOrderContainer().setRowData(0, list);

			}

			@Override
			public void onFailure(Throwable caught) {
				showErrorOnFailure(caught);
			}

		});

	}

	public void displayTemplatesForSelection() {
		selectedOrder = orderSelectionModel.getLastSelectedObject();
		orderService.listOrderTemplates(new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> templates) {
				
				List<TemplateItem> templateItems = new ArrayList<TemplateItem>();
				for (String templateName : templates) {
					templateItems.add(createTemplateItem(templateName));
				}
				display.setTemplateListItems(templateItems);
				display.showTemplateList();

			}

			@Override
			public void onFailure(Throwable caught) {
				showErrorOnFailure(caught);
			}
		});

	}

	TemplateItem createTemplateItem(String templateName) {
		TemplateItem templateItem = new TemplateItem();
		templateItem.setText(templateName);
		templateItem.setClickHandler(new TemplateClickHandler(templateName));
		return templateItem;
	}

	public void createAndDownlaodDocument() {
		orderService.createDocument(selectedOrder, selectedTemplate,
				new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						display.hideTemplateList();

						String url = GWT.getModuleBaseURL() + "orderService?file=" + result.toString();
						url = URL.decode(url);
						Window.open(url, "parent", "");
					}

					@Override
					public void onFailure(Throwable caught) {
						display.hideTemplateList();
						showErrorOnFailure(caught);
					}
				});

	}
	
	private void showErrorOnFailure(Throwable caught){
		String message  = ((caught == null || caught.getMessage()== null || "".equals(caught.getMessage()) 	|| caught.getMessage().length() < 4 ) )?
				Application.MESSAGES.wte4j_message_document_generation_service_unavailable_text():caught.getMessage();
		MessageDialog messageDialog = new MessageDialog(Application.MESSAGES.wte4j_message_document_generation_error_title(), message, DialogType.ERROR);
		messageDialog.show();
	}

	private class TemplateClickHandler implements ClickHandler {

		private String template;

		public TemplateClickHandler(String template) {
			this.template = template;
		}

		@Override
		public void onClick(ClickEvent event) {
			selectedTemplate = template;
			createAndDownlaodDocument();
		}
	}

}
