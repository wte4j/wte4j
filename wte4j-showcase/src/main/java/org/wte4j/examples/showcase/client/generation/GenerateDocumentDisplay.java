package org.wte4j.examples.showcase.client.generation;

import org.wte4j.examples.showcase.shared.OrderDataDto;
import org.wte4j.ui.shared.TemplateDto;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;

public interface GenerateDocumentDisplay extends IsWidget {

	HasData<OrderDataDto> getOrderContainer();

	HasData<TemplateDto> getTemplateContainer();

	void showTemplateList();

	void hideTemplateList();
}
