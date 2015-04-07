package org.wte4j.examples.showcase.client.main;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;

public interface MenuDisplay extends IsWidget {

	void setGenerateDocumentActive(boolean active);

	void setManageTemplatesActive(boolean active);

	void addGenerateDocumentClickHandler(ClickHandler clickHandler);

	void addManageTemplateClickHandler(ClickHandler clickHandler);

}
