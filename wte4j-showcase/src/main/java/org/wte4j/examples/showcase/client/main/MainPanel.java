package org.wte4j.examples.showcase.client.main;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class MainPanel extends Composite implements MainDisplay {

	private static MainPanelUiBinder uiBinder = GWT.create(MainPanelUiBinder.class);

	@UiField
	AnchorListItem generateDocumentAction;

	@UiField
	AnchorListItem manageTemplatesAction;

	@UiField
	FlowPanel contentBody;

	public MainPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setGenerateDocumentActive(boolean active) {
		generateDocumentAction.setActive(active);

	}

	@Override
	public void setManageTemplatesActive(boolean active) {
		manageTemplatesAction.setActive(active);
	}

	@Override
	public void addGenerateDocumentClickHandler(ClickHandler clickHandler) {
		generateDocumentAction.addClickHandler(clickHandler);
	}

	@Override
	public void addManageTemplateClickHandler(ClickHandler clickHandler) {
		manageTemplatesAction.addClickHandler(clickHandler);
	}

	@Override
	public void setContent(IsWidget conent) {
		contentBody.clear();
		contentBody.add(conent);

	}

	interface MainPanelUiBinder extends UiBinder<Widget, MainPanel> {
	}
}
