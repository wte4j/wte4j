package ch.born.wte.ui.client.templates;

import static ch.born.wte.ui.client.Application.LABELS;
import static ch.born.wte.ui.client.Application.RESOURCES;
import ch.born.wte.ui.shared.TemplateDto;

import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;

//import static ch.born.wte.ui.client.Application.*;

public class TemplateTablePanel extends Composite implements
		TemplateListDisplay {

	private static DocumentTemplatePanelUiBinder uiBinder = GWT
			.create(DocumentTemplatePanelUiBinder.class);

	@UiField
	SimplePanel tablePanel;

	@UiField()
	CellTable<TemplateDto> templateTable;

	private DateTimeFormat timeStampFormat = DateTimeFormat
			.getFormat(PredefinedFormat.DATE_TIME_SHORT);

	private PopupPanel contextPanel;
	private TemplateContextMenu templateContextMenu;

	public TemplateTablePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		initContextMenu();
		initTemplateTable();

	}

	private void initContextMenu() {

		templateContextMenu = new TemplateContextMenu();
		contextPanel = new PopupPanel(true);
		contextPanel.add(templateContextMenu);
		contextPanel.setAnimationType(AnimationType.ONE_WAY_CORNER);
		ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				contextPanel.hide();
			}
		};
		addHandler(clickHandler, ClickEvent.getType());
		contextPanel.addHandler(clickHandler, ClickEvent.getType());
		templateContextMenu.addHandler(clickHandler, ClickEvent.getType());
	}

	private void initTemplateTable() {
		TextColumn<TemplateDto> nameColumn = new TextColumn<TemplateDto>() {

			@Override
			public String getValue(TemplateDto template) {
				return template.getDocumentName();
			}

		};
		templateTable.addColumn(nameColumn, LABELS.templateDocumentName());

		TextColumn<TemplateDto> changeDateColumn = new TextColumn<TemplateDto>() {

			@Override
			public String getValue(TemplateDto template) {
				return timeStampFormat.format(template.getUpdatedAt());
			}
		};
		templateTable.addColumn(changeDateColumn, LABELS.templateEditedAt());

		TextColumn<TemplateDto> editorColumn = new TextColumn<TemplateDto>() {

			@Override
			public String getValue(TemplateDto template) {
				return template.getEditor().getDisplayName();
			}
		};
		templateTable.addColumn(editorColumn, LABELS.templateEditor());

		ImageResourceCell imageResourceCell = new ImageResourceCell();
		PopupCell<ImageResource> popupCell = new PopupCell<ImageResource>(
				contextPanel, imageResourceCell);
		Column<TemplateDto, ImageResource> actionColumn = new Column<TemplateDto, ImageResource>(
				popupCell) {
			@Override
			public ImageResource getValue(TemplateDto object) {
				return RESOURCES.tableActionMenu();
			}
		};
		templateTable.addColumn(actionColumn);
	}

	@Override
	public HasData<TemplateDto> getDataContainer() {
		return templateTable;
	}

	@Override
	public void setDowndLoadCommand(ScheduledCommand command) {
		ScheduledCommand commandWithCloseContextMenu = decorateCommandWithCloseContextMenu(command);
		templateContextMenu.getDownloadAction().setScheduledCommand(commandWithCloseContextMenu);
	}

	@Override
	public void setUpdateCommand(ScheduledCommand command) {
		ScheduledCommand commandWithCloseContextMenu = decorateCommandWithCloseContextMenu(command);
		templateContextMenu.getUpdateAction().setScheduledCommand(commandWithCloseContextMenu);

	}

	@Override
	public void setUnlockCommand(ScheduledCommand command) {
		ScheduledCommand commandWithCloseContextMenu = decorateCommandWithCloseContextMenu(command);
		templateContextMenu.getUnlockAction().setScheduledCommand(commandWithCloseContextMenu);

	}

	@Override
	public void setLockCommand(ScheduledCommand command) {
		ScheduledCommand commandWithCloseContextMenu = decorateCommandWithCloseContextMenu(command);
		templateContextMenu.getLockAction().setScheduledCommand(commandWithCloseContextMenu);

	}

	@Override
	public void setDeleteCommand(ScheduledCommand command) {
		ScheduledCommand decoretedCommandWithCloseContextMenu = decorateCommandWithCloseContextMenu(command);
		templateContextMenu.getDeleteAction().setScheduledCommand(decoretedCommandWithCloseContextMenu);

	}
	
	private ScheduledCommand decorateCommandWithCloseContextMenu(final ScheduledCommand command) {
		return new ScheduledCommand() {
			@Override
			public void execute() {
				contextPanel.hide();
				command.execute();
			}
		};
	}

	interface DocumentTemplatePanelUiBinder extends
			UiBinder<Widget, TemplateTablePanel> {
	}

}
