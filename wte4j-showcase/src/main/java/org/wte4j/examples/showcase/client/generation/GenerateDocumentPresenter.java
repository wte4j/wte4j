package org.wte4j.examples.showcase.client.generation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wte4j.examples.showcase.shared.OrderDataDto;

public class GenerateDocumentPresenter {

	private GenerateDocumentDisplay display;

	public void bind(GenerateDocumentDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException("presenter is allready bound");
		}
		display = aDisplay;
		initDataContainer();
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
		display.getDataContainer().setRowData(0, list);

	}

}
