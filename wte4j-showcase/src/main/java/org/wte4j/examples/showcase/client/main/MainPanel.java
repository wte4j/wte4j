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
	FlowPanel mainContent;

	@UiField
	FlowPanel rightContent;

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
	public void setMainContent(IsWidget conent) {
		mainContent.clear();
		mainContent.add(conent);

	}

	@Override
	public void setRightContent(IsWidget content) {
		rightContent.clear();
		rightContent.add(content);
	}

	interface MainPanelUiBinder extends UiBinder<Widget, MainPanel> {
	}
}
