package ch.born.wte.ui.client.templates;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.born.wte.ui.client.Labels;
import ch.born.wte.ui.client.MessageDialog;
import ch.born.wte.ui.shared.TemplateDto;
import ch.born.wte.ui.shared.TemplateService;
import ch.born.wte.ui.shared.TemplateServiceAsync;
import ch.born.wte.ui.shared.UserDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class TemplateTablePanel extends Composite {

	private static DocumentTemplatePanelUiBinder uiBinder = GWT
			.create(DocumentTemplatePanelUiBinder.class);

	@UiField
	FlexTable templateTable;

	@UiField
	ListBox sortSelection;

	TemplateContextMenu contextMenu;

	private DateTimeFormat timeStampFormat = DateTimeFormat
			.getFormat(PredefinedFormat.DATE_TIME_SHORT);

	private Labels labels = GWT.create(Labels.class);

	private TemplateServiceAsync templateService = GWT
			.create(TemplateService.class);

	private List<TemplateDto> templates;

	private int selectedIndex;

	public TemplateTablePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		createFTTemplateHeader();
		initSortListBox();
		initActionPanel();
	}

	public void loadData() {

		templateService.getTemplates(new AsyncCallback<List<TemplateDto>>() {

			@Override
			public void onSuccess(List<TemplateDto> result) {
				templates = result;
				updateTable();
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageDialog dialog = new MessageDialog("", caught
						.getMessage(), MessageDialog.ERROR);
				dialog.show();
			}
		});
	}

	private void updateTable() {
		templateTable.removeAllRows();
		createFTTemplateHeader();
		int row = 1;
		for (final TemplateDto templateDto : templates) {
			setRowData(row, templateDto);
			row++;
		}

	}

	private void initSortListBox() {
		sortSelection.addItem(labels.templateDocumentName());
		sortSelection.addItem(labels.templateLanguage());
		sortSelection.addItem(labels.templateEditedAt());
		sortSelection.addItem(labels.templateEditor());
		sortSelection.addItem(labels.templateLockingUser());
		sortSelection.addItem("*");
		sortSelection.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				sortTemplates();
				updateTable();
			}
		});
	}

	private void initActionPanel() {

		contextMenu = new TemplateContextMenu();
		contextMenu.getDownloadAction().setScheduledCommand(
				createDownloadCommand());
		contextMenu.getLockAction().setScheduledCommand(createLockCommand());
		contextMenu.getUnlockAction()
				.setScheduledCommand(createUnLockCommand());

		contextMenu.getDeleteAction()
				.setScheduledCommand(createDeleteCommand());
	}

	private void createFTTemplateHeader() {
		HTMLTable.RowFormatter rf = templateTable.getRowFormatter();

		Label lblTemplate = new Label();
		lblTemplate.setWidth("250px");
		lblTemplate.setText(labels.templateDocumentName());

		Label lblLanguage = new Label();
		lblLanguage.setWidth("75px");
		lblLanguage.setText(labels.templateLanguage());

		Label lblUptdatedAt = new Label();
		lblUptdatedAt.setWidth("250px");
		lblUptdatedAt.setText(labels.templateEditedAt());

		Label lblEditor = new Label();
		lblEditor.setWidth("250px");
		lblEditor.setText(labels.templateEditor());

		Label lblLockingUser = new Label();
		lblLockingUser.setWidth("250px");
		lblLockingUser.setText(labels.templateLockingUser());

		Label lblIcons = new Label();
		lblIcons.setWidth("75px");
		int row = 0;
		int column = 0;
		templateTable.setWidget(row, column++, lblTemplate);
		templateTable.setWidget(row, column++, lblLanguage);
		templateTable.setWidget(row, column++, lblUptdatedAt);
		templateTable.setWidget(row, column++, lblEditor);
		templateTable.setWidget(row, column++, lblLockingUser);
		templateTable.setWidget(row, column++, lblIcons);

		rf.addStyleName(row, "FlexTable-ColumnLabel");
	}

	private void setRowData(final int row, TemplateDto template) {
		int column = 0;
		HTMLTable.RowFormatter rf = templateTable.getRowFormatter();
		templateTable.setText(row, column++, template.getDocumentName());
		templateTable.setText(row, column++, template.getLanguage());

		String editedDate = timeStampFormat.format(template.getUpdatedAt());
		templateTable.setText(row, column++, editedDate);
		templateTable.setText(row, column++, template.getEditor()
				.getDisplayName());

		String lockingUser = "";
		if (template.getLockingUser() != null) {
			lockingUser = template.getLockingUser().getDisplayName();
		}
		templateTable.setText(row, column++, lockingUser);

		Button button = new Button("actions");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				HTMLTable.Cell cell = templateTable.getCellForEvent(event);
				int row = cell.getRowIndex();
				if (row > 0) {
					selectedIndex = row - 1;
					Widget source = (Widget) event.getSource();

					contextMenu.popup(source);
				}
			}
		});
		templateTable.setWidget(row, column++, button);

		if ((row % 2) != 0) {
			rf.addStyleName(row, "FlexTable-OddRow");
		} else {
			rf.addStyleName(row, "FlexTable-EvenRow");
		}
	}

	private ScheduledCommand createDownloadCommand() {

		return new ScheduledCommand() {
			@Override
			public void execute() {
				Window.open(getSelectedTemplate().getTemplatePath(), "_blank",
						null);
			}
		};
	}

	private ScheduledCommand createLockCommand() {
		return new ScheduledCommand() {
			@Override
			public void execute() {
				final int templateIndex = selectedIndex;
				templateService.lockTemplate(getSelectedTemplate(),
						new AsyncCallback<TemplateDto>() {

							@Override
							public void onSuccess(TemplateDto result) {
								templates.set(templateIndex, result);
								setRowData(templateIndex + 1, result);
							}

							@Override
							public void onFailure(Throwable caught) {
								new MessageDialog("", caught.getMessage(),
										MessageDialog.ERROR).show();

							}
						});
			}
		};
	}

	private ScheduledCommand createUnLockCommand() {
		return new ScheduledCommand() {
			@Override
			public void execute() {
				final int templateIndex = selectedIndex;
				templateService.unlockTemplate(getSelectedTemplate(),
						new AsyncCallback<TemplateDto>() {

							@Override
							public void onSuccess(TemplateDto result) {
								templates.set(templateIndex, result);
								setRowData(templateIndex + 1, result);
							}

							@Override
							public void onFailure(Throwable caught) {
								new MessageDialog("", caught.getMessage(),
										MessageDialog.ERROR).show();
								;

							}
						});
			}
		};
	}

	private ScheduledCommand createDeleteCommand() {
		return new ScheduledCommand() {
			@Override
			public void execute() {
				final TemplateDto template = getSelectedTemplate();
				templateService.deleteTemplate(template,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								templates.remove(template);
								updateTable();
							}

							@Override
							public void onFailure(Throwable caught) {
								new MessageDialog("", caught.getMessage(),
										MessageDialog.ERROR).show();
								;

							}
						});
			}
		};
	}

	TemplateDto getSelectedTemplate() {
		return templates.get(selectedIndex);
	}

	private void sortTemplates() {
		int index = sortSelection.getSelectedIndex();
		switch (index) {
		case 0:
			Collections.sort(templates, new Comparator<TemplateDto>() {

				@Override
				public int compare(TemplateDto o1, TemplateDto o2) {
					return o1.getDocumentName().toLowerCase()
							.compareTo(o2.getDocumentName().toLowerCase());
				}
			});
			break;
		case 1:
			Collections.sort(templates, new Comparator<TemplateDto>() {

				@Override
				public int compare(TemplateDto o1, TemplateDto o2) {
					return o1.getLanguage().compareTo(o2.getLanguage());
				}
			});
			break;
		case 2:
			Collections.sort(templates, new Comparator<TemplateDto>() {

				@Override
				public int compare(TemplateDto o1, TemplateDto o2) {
					return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
				}
			});
			break;
		case 3:
			Collections.sort(templates, new Comparator<TemplateDto>() {

				@Override
				public int compare(TemplateDto o1, TemplateDto o2) {
					return o1.getEditor().getDisplayName()
							.compareTo(o2.getEditor().getDisplayName());
				}
			});
			break;
		case 4:
			Collections.sort(templates, new Comparator<TemplateDto>() {

				@Override
				public int compare(TemplateDto o1, TemplateDto o2) {
					UserDto user1 = o1.getLockingUser();
					UserDto user2 = o2.getLockingUser();
					if (user1 != null && user2 != null) {
						return user1.getDisplayName().compareTo(
								user2.getDisplayName());
					} else if (user1 != null) {
						return -1;
					} else {
						return 1;
					}
				}
			});
			break;
		default:
			break;
		}
	}

	interface DocumentTemplatePanelUiBinder extends
			UiBinder<Widget, TemplateTablePanel> {
	}

}
