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
package ch.born.wte.ui.client.templates.contextmenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

public class TemplateContextMenu extends Composite {

	private static TemplateContextMenuUiBinder uiBinder = GWT
			.create(TemplateContextMenuUiBinder.class);

	@UiField
	MenuBar contextMenu;

	@UiField
	MenuItem downloadAction;

	@UiField
	MenuItem updateAction;

	@UiField
	MenuItem lockAction;

	@UiField
	MenuItem unlockAction;

	@UiField
	MenuItem deleteAction;

	public TemplateContextMenu() {
		initWidget(uiBinder.createAndBindUi(this));		

		int lockIndex = contextMenu.getItemIndex(lockAction);
		contextMenu.insertSeparator(lockIndex);

		int deleteIndex = contextMenu.getItemIndex(deleteAction);
		contextMenu.insertSeparator(deleteIndex);

	}

	public MenuItem getDownloadAction() {
		return downloadAction;
	}

	public MenuItem getUpdateAction() {
		return updateAction;
	}

	public MenuItem getLockAction() {
		return lockAction;
	}

	public MenuItem getUnlockAction() {
		return unlockAction;
	}

	public MenuItem getDeleteAction() {
		return deleteAction;
	}



	interface TemplateContextMenuUiBinder extends
			UiBinder<Widget, TemplateContextMenu> {
	}

}
