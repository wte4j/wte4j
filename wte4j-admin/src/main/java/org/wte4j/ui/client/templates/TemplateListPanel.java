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
package org.wte4j.ui.client.templates;

import static org.wte4j.ui.client.Application.LABELS;

import java.util.Date;

import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.wte4j.ui.client.cell.PopupCell;
import org.wte4j.ui.client.templates.contextmenu.TemplateContextMenu;
import org.wte4j.ui.client.templates.mapping.MappingDisplay;
import org.wte4j.ui.client.templates.mapping.MappingPanel;
import org.wte4j.ui.client.templates.upload.TemplateUploadDisplay;
import org.wte4j.ui.client.templates.upload.TemplateUploadFormPanel;
import org.wte4j.ui.shared.TemplateDto;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;

public class TemplateListPanel extends Composite implements
		TemplateListDisplay {

	private static TemplateTablePanelUiBInder uiBinder = GWT
			.create(TemplateTablePanelUiBInder.class);

	@UiField
	CellTable<TemplateDto> templateTable;

	@UiField
	MyStyle style;
	private Column<TemplateDto, String> nameColumn;
	private Column<TemplateDto, Date> editedAtColumn;
	private Column<TemplateDto, String> editorColumn;
	private TextColumn<TemplateDto> statusColumn;
	private Column<TemplateDto, ?> actionColumn;

	@UiField
	Pagination templateTablePagination;
	private SimplePager internalPager;

	private DateTimeFormat timeStampFormat = DateTimeFormat
			.getFormat(PredefinedFormat.DATE_TIME_SHORT);

	@UiField
	PopupPanel contextPanel;
	private TemplateContextMenu templateContextMenu;

	@UiField
	Modal modalPanel;
	@UiField
	ModalBody modalBody;

	private TemplateUploadDisplay templateUploadDisplay = new TemplateUploadFormPanel("wte4-admin-template-update");;
	private MappingDisplay mappingDisplay = new MappingPanel();

	public TemplateListPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		initContextMenu();
		initTemplateTable();
	}

	private void initContextMenu() {
		templateContextMenu = new TemplateContextMenu();
		contextPanel.add(templateContextMenu);
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				contextPanel.hide();

			}
		};

		contextPanel.addHandler(clickHandler, ClickEvent.getType());
		templateContextMenu.addHandler(clickHandler, ClickEvent.getType());
	}

	private void initTemplateTable() {
		templateTable.addStyleName("wte-cellTable");

		internalPager = new SimplePager();
		internalPager.setRangeLimited(true);
		internalPager.setPageSize(templateTable.getPageSize());
		internalPager.setDisplay(templateTable);

		initNameColumn();
		initEditedAtColumn();
		initEditorColumn();
		initStatusColumn();
		initActionColumn();

		templateTable.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {

			@Override
			public void onRowCountChange(RowCountChangeEvent event) {
				templateTablePagination.rebuild(internalPager);
			}
		});

		templateTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				templateTablePagination.rebuild(internalPager);
			}
		});
	}

	private void initNameColumn() {
		nameColumn = new TextColumn<TemplateDto>() {

			@Override
			public String getValue(TemplateDto template) {
				return template.getDocumentName();
			}

		};
		nameColumn.setCellStyleNames("templates-name-cell");
		addColumntoTemplateTable(nameColumn, LABELS.templateDocumentName());
	}

	private void initEditedAtColumn() {
		DateCell dateCell = new DateCell(timeStampFormat);
		editedAtColumn = new Column<TemplateDto, Date>(dateCell) {

			@Override
			public Date getValue(TemplateDto object) {
				return object.getUpdatedAt();
			}
		};
		editedAtColumn.setCellStyleNames("templates-editedAt-cell");
		editedAtColumn.setHorizontalAlignment(Column.ALIGN_LEFT);
		addColumntoTemplateTable(editedAtColumn, LABELS.templateEditedAt());

	}

	private void initEditorColumn() {
		editorColumn = new TextColumn<TemplateDto>() {

			@Override
			public String getValue(TemplateDto template) {
				return template.getEditor().getDisplayName();
			}
		};
		editorColumn.setCellStyleNames("templates-editor-cell");
		editorColumn.setHorizontalAlignment(Column.ALIGN_LEFT);
		addColumntoTemplateTable(editorColumn, LABELS.templateEditor());
	}

	private void initActionColumn() {
		ButtonCell buttonCell = new ButtonCell(IconType.COG, ButtonType.LINK, ButtonSize.DEFAULT);
		PopupCell<String> popupCell = new PopupCell<String>(
				contextPanel, buttonCell);
		actionColumn = new Column<TemplateDto, String>(
				popupCell) {
			@Override
			public String getValue(TemplateDto object) {
				return "";
			}
		};
		actionColumn.setCellStyleNames("templates-action-cell " + style.templatesActionCell());
		addColumntoTemplateTable(actionColumn, "");
	}

	private void initStatusColumn() {
		statusColumn = new TextColumn<TemplateDto>() {

			@Override
			public String getValue(TemplateDto template) {
				String lockingUserName = "";
				if (template.getLockingUser() != null) {
					lockingUserName = template.getLockingUser().getDisplayName();
				}
				return lockingUserName;
			}
		};
		statusColumn.setCellStyleNames("locking-user-cell");
		addColumntoTemplateTable(statusColumn, LABELS.templateLockingUser());
	}

	private void addColumntoTemplateTable(Column<TemplateDto, ?> column, String headerText) {
		Header<String> header = new TextHeader(headerText);
		templateTable.addColumn(column, header);
	}

	@Override
	public HasData<TemplateDto> getDataContainer() {
		return templateTable;
	}

	@Override
	public void setDowndLoadCommand(final ClickHandler command) {
		templateContextMenu.getDownloadAction().addClickHandler(wrapClickHandler(command));
	}

	@Override
	public void setUpdateCommand(ClickHandler command) {
		templateContextMenu.getUpdateAction().addClickHandler(wrapClickHandler(command));
	}

	@Override
	public void setUnlockCommand(ClickHandler command) {
		templateContextMenu.getUnlockAction().addClickHandler(wrapClickHandler(command));
	}

	@Override
	public void setLockCommand(ClickHandler command) {
		templateContextMenu.getLockAction().addClickHandler(wrapClickHandler(command));
	}

	@Override
	public void setDeleteCommand(ClickHandler command) {
		templateContextMenu.getDeleteAction().addClickHandler(wrapClickHandler(command));
	}

	@Override
	public void setUnLockCommandVisible(boolean visible) {
		templateContextMenu.getUnlockAction().setVisible(visible);
	}

	@Override
	public void setLockCommandVisible(boolean visible) {
		templateContextMenu.getLockAction().setVisible(visible);
	}

	@Override
	public void addContextMenuCloseHandler(CloseHandler<PopupPanel> handler) {
		contextPanel.addCloseHandler(handler);
	}

	@Override
	public TemplateUploadDisplay getTemplateUploadDisplay() {
		return templateUploadDisplay;
	}

	@Override
	public void showTemplateUploadDisplay(String title) {
		initModelPanel(title, templateUploadDisplay);

	}

	@Override
	public void hideTemplateUploadDisplay() {
		hideModalPanel();
	}

	@Override
	public void showMappingDisplay(String title) {
		initModelPanel(title, mappingDisplay);
	}

	@Override
	public void hideMappingDisplay() {
		hideModalPanel();
	}

	@Override
	public MappingDisplay getMappingDisplay() {
		return mappingDisplay;
	}

	private void initModelPanel(String title, IsWidget content) {
		modalPanel.setTitle(title);
		modalPanel.setDataBackdrop(ModalBackdrop.STATIC);
		modalBody.add(content);
		modalPanel.show();
	}

	private void hideModalPanel() {
		modalPanel.hide();
		modalBody.clear();
	}

	private ClickHandler wrapClickHandler(final ClickHandler toWrap) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				contextPanel.hide();
				toWrap.onClick(event);

			}
		};
	}

	interface MyStyle extends CssResource {
		String templatesActionCell();
	}

	interface TemplateTablePanelUiBInder extends UiBinder<Widget, TemplateListPanel> {
	}

}
