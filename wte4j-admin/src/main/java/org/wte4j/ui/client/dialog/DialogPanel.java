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
package org.wte4j.ui.client.dialog;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

class DialogPanel extends Composite {

	private static DialogPanelUiBinder uiBinder = GWT.create(DialogPanelUiBinder.class);
	@UiField
	Icon icon;

	@UiField
	FlowPanel contentPanel;

	@UiField
	Button okButton;

	@UiField
	Button cancelButton;

	public DialogPanel(DialogType type, Widget content) {
		initWidget(uiBinder.createAndBindUi(this));

		icon.setType(type.getIcon());
		icon.addStyleName(type.colorStyle().getCssName());

		contentPanel.add(content);
	}

	public void addOkClickHandler(ClickHandler clickHandler) {
		okButton.setVisible(true);
		okButton.addClickHandler(clickHandler);
	}

	public void addCancelClickHandler(ClickHandler clickHandler) {
		cancelButton.setVisible(true);
		cancelButton.addClickHandler(clickHandler);
	}

	interface DialogPanelUiBinder extends UiBinder<Widget, DialogPanel> {
	}

}
