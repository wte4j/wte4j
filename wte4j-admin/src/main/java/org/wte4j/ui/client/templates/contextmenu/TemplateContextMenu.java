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
package org.wte4j.ui.client.templates.contextmenu;

import org.gwtbootstrap3.client.ui.LinkedGroupItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TemplateContextMenu extends Composite {

	private static TemplateContextMenuUiBinder uiBinder = GWT
			.create(TemplateContextMenuUiBinder.class);

	@UiField
	LinkedGroupItem downloadAction;

	@UiField
	LinkedGroupItem updateAction;

	@UiField
	LinkedGroupItem lockAction;

	@UiField
	LinkedGroupItem unlockAction;

	@UiField
	LinkedGroupItem deleteAction;

	public TemplateContextMenu() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public LinkedGroupItem getDownloadAction() {
		return downloadAction;
	}

	public LinkedGroupItem getUpdateAction() {
		return updateAction;
	}

	public LinkedGroupItem getLockAction() {
		return lockAction;
	}

	public LinkedGroupItem getUnlockAction() {
		return unlockAction;
	}

	public LinkedGroupItem getDeleteAction() {
		return deleteAction;
	}

	interface TemplateContextMenuUiBinder extends
			UiBinder<Widget, TemplateContextMenu> {
	}

}
