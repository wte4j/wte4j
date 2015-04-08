package org.wte4j.examples.showcase.client.generation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wte4j.examples.showcase.shared.OrderDataDto;
import org.wte4j.ui.shared.TemplateDto;

import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

public class GenerateDocumentPresenter {

	private GenerateDocumentDisplay display;
	private NoSelectionModel<OrderDataDto> orderSelectionModel;
	private NoSelectionModel<TemplateDto> templateSelectionModel;

	OrderDataDto selected;

	public void bind(GenerateDocumentDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException("presenter is allready bound");
		}
		display = aDisplay;
		bindOrderContainer();
		bindTemplateContainer();
		initDataContainer();
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

	private void bindTemplateContainer() {
		templateSelectionModel = new NoSelectionModel<TemplateDto>();
		templateSelectionModel.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				generateDocument();
			}

		});
		display.getTemplateContainer().setSelectionModel(templateSelectionModel);

	}

	public void initDataContainer() {
		OrderDataDto dto = new OrderDataDto();
		dto.setFirstName("Hans");
		dto.setLastName("Wurst");
		dto.setAddress("Morgenstrasse 129");
		dto.setCity("Bern");
		dto.setZip("3018");
		dto.setOrderDate(new Date());
		dto.setDeliveryDate(new Date());
		dto.setAmount(6.50);

		List<OrderDataDto> list = new ArrayList<OrderDataDto>();
		list.add(dto);
		display.getOrderContainer().setRowData(0, list);

	}

	public void displayTemplatesForSelection() {
		selected = orderSelectionModel.getLastSelectedObject();
		TemplateDto templateDto = new TemplateDto();
		templateDto.setDocumentName("Template");

		List<TemplateDto> templateList = new ArrayList<TemplateDto>();
		templateList.add(templateDto);
		display.getTemplateContainer().setRowData(0, templateList);
		display.showTemplateList();
	}

	public void generateDocument() {
		TemplateDto template = templateSelectionModel.getLastSelectedObject();
		display.getTemplateContainer().setRowData(0, new ArrayList<TemplateDto>());
		display.hideTemplateList();
	}

}
