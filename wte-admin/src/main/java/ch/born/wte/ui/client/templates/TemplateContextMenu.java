package ch.born.wte.ui.client.templates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
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

	private PopupPanel contextPanel;

	public TemplateContextMenu() {
		initWidget(uiBinder.createAndBindUi(this));
		contextPanel = new PopupPanel(true);
		contextPanel.add(contextMenu);
		contextMenu.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				contextPanel.hide();

			}
		}, ClickEvent.getType());

		int lockIndex = contextMenu.getItemIndex(lockAction);
		contextMenu.insertSeparator(lockIndex);

		int deleteIndex = contextMenu.getItemIndex(deleteAction);
		contextMenu.insertSeparator(deleteIndex);

	}

	interface TemplateContextMenuUiBinder extends
			UiBinder<Widget, TemplateContextMenu> {
	}

	public static TemplateContextMenuUiBinder getUiBinder() {
		return uiBinder;
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

	public void hide() {
		contextPanel.hide();
	}

	public void popup(final Widget widget) {
		contextPanel.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = widget.getAbsoluteLeft() + widget.getOffsetWidth()
						- contextMenu.getOffsetWidth();
				int top = widget.getAbsoluteTop();
				contextPanel.setPopupPosition(left, top);

			}
		});

	}

}
