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
