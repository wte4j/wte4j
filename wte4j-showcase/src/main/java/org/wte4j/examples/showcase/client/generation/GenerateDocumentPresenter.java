package org.wte4j.examples.showcase.client.generation;

import java.util.ArrayList;
import java.util.List;

import org.wte4j.examples.showcase.shared.OrderDataDto;
import org.wte4j.examples.showcase.shared.service.OrderServiceAsync;

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
			throw new IllegalStateException("presenter is allready bound");
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
				Window.alert(caught.getMessage());
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
				Window.alert(caught.getMessage());
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
		display.hideTemplateList();
		orderService.createDocument(selectedOrder, selectedTemplate,
				new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						String url = GWT.getModuleBaseURL() + "orderService?file=" + result.toString();
						url = URL.decode(url);
						Window.open(url, "parent", "");
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
				});

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
